package in.forpay.fragment.shop;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.forpay.R;
import in.forpay.activity.shop.ShopOrderActivity;
import in.forpay.activity.shop.ShopReviewActivity;
import in.forpay.adapter.shop.ShopChatListAdapter;
import in.forpay.databinding.FragmentChatBinding;
import in.forpay.databinding.LayoutSendImageDialogBinding;
import in.forpay.listener.ChatImageListener;
import in.forpay.model.shop.ShopChatListModel;
import in.forpay.model.shop.ShopMessageSendModel;
import in.forpay.model.shop.ShopOrderModel;
import in.forpay.model.supportchatModel.ChatModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

import static android.app.Activity.RESULT_OK;

public class ChatFragment extends Fragment implements ChatImageListener {

    private FragmentChatBinding binding;
    private Activity activity;
    private ProgressHelper progressHelper;
    private String shopId,orderId="",rating="",lastActive,image,shopName,contactNumber,toUserId,shopOrderModel;
    private ArrayList<ChatModel> chatListModelList;
    private ShopChatListAdapter adapter;
    private File directory;
    private ArrayList<ShopChatListModel.DataBean> chatList;
    private PowerManager pm;
    volatile boolean activityStopped = false;
    private Handler handler;
    private Runnable runnable;
    private String TAG="ShopListChatActivity";
    private MediaRecorder mRecorder = null;
    private static String mFileName = null;
    private File audioDirectory;
    LinearLayoutManager linearLayoutManager ;
    boolean recyclerViewLastPosition;
    private final static int REFRESH_CHAT_LIST_API_MILLISECOND=15000;
    private boolean isSecondTime;
    private boolean isFirstTimeLoader;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_chat, container, false);
        activity=getActivity();

        progressHelper=new ProgressHelper(activity);
        linearLayoutManager=new LinearLayoutManager(activity);

        final Bundle args = getArguments();
        assert args != null;
        shopId=args.getString(Constant.SHOP_ID);
        shopOrderModel=args.getString(Constant.SHOP_ORDER_MODEL);
        toUserId=args.getString(Constant.TO_USER_ID);

        checkReadWritePermission();

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPosition;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                lastPosition = linearLayoutManager.findLastVisibleItemPosition();

                if (chatList!=null)
                    recyclerViewLastPosition= lastPosition == chatList.size() - 1;
            }
        });

        return binding.getRoot();
    }

    private void init() {
        setShopChatAdapter();
        getShopChatHistory();
        if (shopOrderModel!=null)
            parseShopOrderResponse(shopOrderModel);
        //binding.backPress.setOnClickListener(v->onBackPressed());

        /*binding.ratingLayout.setOnClickListener(v-> StartAepsActivity(new Intent(activity, ShopReviewActivity.class)
                .putExtra(Constant.SHOP_ID,shopId)
                .putExtra(Constant.ORDER_ID,orderId)));*/

        binding.sendMessage.setOnClickListener(v->{
            if (binding.edtMessage.getText().toString().trim().isEmpty()) {
                Utility.showToast(activity, "Please type message.", "");
            } else {
                sendMessage();
                postUserMessage(binding.edtMessage.getText().toString(), "", "0");
                binding.edtMessage.getText().clear();
            }
        });

        binding.selectImage.setOnClickListener(v->{
            Utility.imagePickerIntent(activity);
        });

        final long[] touchtime = {System.currentTimeMillis()};
        binding.audio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    touchtime[0] = System.currentTimeMillis();
                    Runbeep("start");
                    setButtonEnableDisable(false);
                    //Toast.makeText(activity, "Recording...", Toast.LENGTH_SHORT).show();

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (touchtime[0] + 1000 > System.currentTimeMillis()) {
                        stop_timer();
                        Toast.makeText(activity, "Hold Mic Button to Record", Toast.LENGTH_SHORT).show();
                    } else {
                        stopRecording();
                        //Toast.makeText(activity, "Stop Recording...", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }

        });

        directory = new File(Environment.getExternalStorageDirectory() + File.separator
                + getResources().getString(R.string.app_name) + File.separator + Constant.SHOP_DIRECTORY_NAME);

        pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);

        makeDir();
    }

    private void checkReadWritePermission() {

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Log.d(TAG, "checkPermission: onPermissionGranted");
                init();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Log.d(TAG, "checkPermission: onPermissionDenied");
                Utility.showToast(activity, "Permission Denied", "");
            }
        };

        TedPermission.with(activity)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\n" +
                        "Please turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                .check();
    }

    // this function will start the recrding
    private void startRecording() {

        audioDirectory = new File(Environment.getExternalStorageDirectory() + File.separator
                + getResources().getString(R.string.app_name) + File.separator + Constant.SHOP_DIRECTORY_NAME + File.separator +Constant.SHOP_AUDIO_DIRECTORY_NAME);

        if (!audioDirectory.exists()) {
            audioDirectory.mkdirs();
        }

        mFileName = String.valueOf(new File(Environment.getExternalStorageDirectory() + File.separator
                + getResources().getString(R.string.app_name) + File.separator + Constant.SHOP_DIRECTORY_NAME + File.separator +Constant.SHOP_AUDIO_DIRECTORY_NAME));
        mFileName += "/shop.mp3";

        try{
            if(mRecorder!=null) {
                mRecorder.stop();
                mRecorder.reset();
                mRecorder.release();
                mRecorder=null;
            }

            mRecorder = new MediaRecorder();

            if(mRecorder!=null)
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

            if(mRecorder!=null)
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

            if(mRecorder!=null)
                mRecorder.setOutputFile(mFileName);

            if(mRecorder!=null)
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                if(mRecorder!=null)
                    mRecorder.prepare();
            } catch (IOException e) {
                Log.e("resp", "prepare() failed");
            }
            if(mRecorder!=null)
                mRecorder.start();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void stop_timer(){
        try {
            if (mRecorder != null) {
                mRecorder.stop();
                mRecorder.reset();
                mRecorder.release();
                mRecorder = null;
            }

            if (handler != null && runnable != null)
                handler.removeCallbacks(runnable);

            binding.edtMessage.setText(null);

            if (timer != null) {
                timer.cancel();
                binding.edtMessage.setText(null);
            }
            setButtonEnableDisable(true);
        }catch (Exception e){
            e.printStackTrace();
            setButtonEnableDisable(true);
        }
    }

    private void setButtonEnableDisable(boolean isEnable){
        binding.sendMessage.setEnabled(isEnable);
        binding.edtMessage.setEnabled(isEnable);
        binding.selectImage.setEnabled(isEnable);
    }
    public void stop_timer_without_recoder(){

        if(handler!=null && runnable!=null)
            handler.removeCallbacks(runnable);

        binding.edtMessage.setText(null);

        if(timer!=null){
            timer.cancel();
            binding.edtMessage.setText(null);
        }
        setButtonEnableDisable(true);
    }

    public void stopRecording() {
        try {
            stop_timer_without_recoder();
            if(mRecorder!=null ) {
                mRecorder.stop();
                mRecorder.reset();
                mRecorder.release();
                mRecorder=null;
                Runbeep("stop");
                sendAudio(mFileName);
                postUserMessage("", mFileName, "3");
                setButtonEnableDisable(true);
            }
        }catch (Exception e){
            e.printStackTrace();
            setButtonEnableDisable(true);
        }

    }

    public void Runbeep(final String action){

        // within 700 milisecond the timer will be start
        handler=new Handler();
        if(action.equals("start")) {
            binding.edtMessage.setText("00:00");
            runnable = new Runnable() {
                @Override
                public void run() {
                    start_timer();
                }
            };

            handler.postDelayed(runnable, 700);
        }


        // this will run a beep sound
        final MediaPlayer beep = MediaPlayer.create(activity, R.raw.sound);
        beep.setVolume(100,100);
        beep.start();
        beep.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                beep.release();

                // if our action is start a recording the recording will start
                if(action.equals("start"))
                    startRecording();
            }
        });
    }

    CountDownTimer timer;
    public void start_timer() {

        timer=new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long time=30000-millisUntilFinished;

                int min = (int) (time/1000) / 60;
                int sec = (int) (time/1000) % 60;
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", min, sec);
                binding.edtMessage.setText(timeLeftFormatted);
            }

            @Override
            public void onFinish() {
                stopRecording();
                binding.edtMessage.setText(null);
            }
        };

        timer.start();
    }

    private void handler() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!activityStopped) {
                    boolean isScreenOn = true;
                    handler.postDelayed(this, REFRESH_CHAT_LIST_API_MILLISECOND);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                        if (pm != null) {
                            isScreenOn = pm.isInteractive();
                            Log.d("Chatlist", "Screen is " + isScreenOn);
                        }
                    }
                    if (isScreenOn) {
                        Log.d("Chatlist", "Screen on");
                        getShopChatHistory();
                    }
                }
            }
        };

        handler.postDelayed(runnable, REFRESH_CHAT_LIST_API_MILLISECOND);
    }

    private void postUserMessage(String message, String filePath, String msg_type) {
        if (Utility.isNetworkConnected(activity)) {
            String msg = message;
            msg = escapeJavaString(msg);

            ArrayList<String> list = new ArrayList<>();
            list.add("token=" + Utility.getToken(activity));
            list.add("imei=" + Utility.getImei(activity));
            list.add("appVersion=" + Utility.getVersionCode(activity));
            list.add("shopId=" + shopId);
            list.add("message=" + msg);
            list.add("msgType=" + msg_type);

            String request = Utility.dataWrapper(list);

            postUserMessageRequest(request, filePath, msg_type);

        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
    }

    private void postUserMessageRequest(String request, String filePath, String msgType) {
        if (Utility.isNetworkConnected(activity)) {
            if (!filePath.isEmpty()) {
                QueryManager.getInstance().filePath = filePath;
                QueryManager.getInstance().fileType = msgType.equals("1") ? "Image" :"Audio";
            }

            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_POST_SHOP_CHAT, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            postUserMessageResponse(result, responseManager, filePath,msgType.equals("1") ? "Image" :"Audio");
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
    }

    private void postUserMessageResponse(String response, ResponseManager responseManager, String filePath,String msgType) {
        try {
            File file = null;

            if (!filePath.isEmpty()) {
                file = new File(filePath);
                long fileSize = file.length() / (1024);
            }

            ShopMessageSendModel messageSendModel = new Gson().fromJson(response, ShopMessageSendModel.class);
            if (messageSendModel.getResCode().equals(Constant.CODE_200)) {
                if (file != null) {

                    prepareCopyFile(messageSendModel.getData().getId(), file,msgType);
                }
                notifyMessage(messageSendModel, filePath);
            } else {
                Utility.showToast(activity, messageSendModel.getResText(), "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareCopyFile(String id, File finalFile,String msgType) {
        try {
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String destfileName;
            if (msgType.equals("Image"))
                destfileName = directory + File.separator + id + ".png";
            else
                destfileName = audioDirectory + File.separator + id + ".mp3";
            File destFile = new File(destfileName);
            copy(finalFile, destFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copy(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }

    private void makeDir() {
        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (!directory.exists()) {
                directory.mkdirs();
            }
        }
    }

    private void notifyMessage(ShopMessageSendModel messageSendModel, String filePath) {
        if (messageSendModel.getData().getMsgType().equals("0")) {
            ChatModel chatModel = new ChatModel(messageSendModel.getData().getId(), messageSendModel.getData().getMsg(), "reply", messageSendModel.getData().getTime()
                    , messageSendModel.getData().getMsgType(), true, "", "", "","");
            chatListModelList.set((chatListModelList.size() - 1), chatModel);
            adapter.notifyDataSetChanged();
        } else {
            ChatModel chatModel = new ChatModel(messageSendModel.getData().getId(), messageSendModel.getData().getMsg(), "reply", messageSendModel.getData().getTime()
                    , messageSendModel.getData().getMsgType(), true, "", filePath, "","");
            chatListModelList.set((chatListModelList.size() - 1), chatModel);
            adapter.notifyDataSetChanged();
        }

        if (!isSecondTime) {
            isSecondTime=true;
            if (chatListModelList == null && chatListModelList.size()==0) {
                Utility.inflateNoDataFoundLayout(activity, binding.inflateLayout, "No Chat Data Found");
            } else {
                binding.inflateLayout.removeAllViews();
                binding.inflateLayout.addView(binding.recyclerView);
            }
        }
    }

    public String escapeJavaString(String st) {
        StringBuilder builder = new StringBuilder();
        try {
            for (int i = 0; i < st.length(); i++) {
                char c = st.charAt(i);
                if (Character.isLetterOrDigit(c) || Character.isSpaceChar(c) || Character.isWhitespace(c) || Character.isAlphabetic(c) || Character.isUnicodeIdentifierPart(c)) {
                    builder.append(c);
                } else {
                    String unicode = String.valueOf(c);
                    int code = (int) c;
                    if (!(code >= 0 && code <= 255)) {
                        unicode = "\\\\u" + Integer.toHexString(c);
                    }
                    builder.append(unicode);
                }
            }
            Log.i("Unicode Block", builder.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return builder.toString();
    }

    private void setShopChatAdapter() {
        chatListModelList = new ArrayList<>();
        linearLayoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ShopChatListAdapter(activity, chatListModelList,this);
        binding.recyclerView.setAdapter(adapter);
    }

    private void sendMessage() {
        ChatModel chatModel = new ChatModel("", binding.edtMessage.getText().toString(), "reply", ""
                , "0", false, "", "", "local","");
        chatListModelList.add(chatModel);
        binding.recyclerView.smoothScrollToPosition((chatListModelList.size() - 1));
        adapter.notifyDataSetChanged();
    }

    private void getShopChatHistory() {
        if (Utility.isNetworkConnected(activity)) {

            ArrayList<String> list = new ArrayList<>();
            list.add("token=" + Utility.getToken(activity));
            list.add("imei=" + Utility.getImei(activity));
            list.add("appVersion=" + Utility.getVersionCode(activity));
            list.add("shopId=" + shopId);
            String request = Utility.dataWrapper(list);
            getShopChatRequest(request);

        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
    }

    private void getShopChatRequest(String request) {
        if (!isFirstTimeLoader) {
            isFirstTimeLoader=true;
            progressHelper.show();
        }
        if (Utility.isNetworkConnected(activity)) {
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_GET_SHOP_CHAT_HISTORY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseGetShopChatResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
    }

    private void parseGetShopChatResponse(String response, ResponseManager responseManager) {
        try {
            Log.d("GetShopChatResponse","response "+response);
            ShopChatListModel chatListModel = new Gson().fromJson(response, ShopChatListModel.class);
            //Log.d("Chatlist","1-"+chatListModel.getResCode()+" - "+chatListModel.getData().size()+"respinse"+response);
            if (chatListModel.getResCode().equals(Constant.CODE_200)) {
                //Log.d("Chatlist",""+chatListModel.getResCode()+" - "+chatListModel.getData().size());
                chatList = new ArrayList<>();
                if (chatListModel.getData().size() > chatList.size()) {
                    chatList.addAll(chatListModel.getData());
                    createChatList();
                }else {
                    progressHelper.dismiss();
                }

                if (chatList.size()==0){
                    progressHelper.dismiss();
                    Utility.inflateNoDataFoundLayout(activity,binding.inflateLayout,"No Chat Data Found");
                }

            } else {
                progressHelper.dismiss();
                Utility.inflateNoDataFoundLayout(activity,binding.inflateLayout,"No Chat Data Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            progressHelper.dismiss();
        }
    }

    private void createChatList() {
        chatListModelList.clear();
        for (ShopChatListModel.DataBean dataBean : chatList) {
            if (dataBean.getMsgType().equals("0")) {
                ChatModel chatListModel = new ChatModel(dataBean.getId(), dataBean.getMsg(), dataBean.getChatType(), dataBean.getTime()
                        , dataBean.getMsgType(), true, dataBean.getTime(), "", "","");
                chatListModelList.add(chatListModel);
            } else if (dataBean.getMsgType().equals("1")){
                ChatModel chatListModel = new ChatModel(dataBean.getId(), dataBean.getMsg(), dataBean.getChatType(), dataBean.getTime()
                        , dataBean.getMsgType(), true, dataBean.getTime(), getImageFilePath(dataBean.getId()), "","");
                chatListModelList.add(chatListModel);
            } else {
                ChatModel chatListModel = new ChatModel(dataBean.getId(), dataBean.getMsg(), dataBean.getChatType(), dataBean.getTime()
                        , dataBean.getMsgType(), true, dataBean.getTime(), getAudioFilePath(dataBean.getId()), "","");
                chatListModelList.add(chatListModel);
            }
        }
        setAdapter(chatListModelList);
    }

    private void setAdapter(ArrayList<ChatModel> chatListModelList) {
        try {
            progressHelper.dismiss();
            if (recyclerViewLastPosition)
                binding.recyclerView.smoothScrollToPosition((chatListModelList.size() - 1));
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getImageFilePath(String id) {
        String filePath = directory + File.separator + id + ".png";
        File file = new File(filePath);
        if (file.exists()) {
            return filePath;
        }
        return "";
    }

    private String getAudioFilePath(String id) {
        String filePath = audioDirectory + File.separator + id + ".mp3";
        File file = new File(filePath);
        if (file.exists()) {
            return filePath;
        }
        return "";
    }

    @Override
    public void notifyDownloadItemImage(ChatModel chatModel, int position) {
        ChatModel chatListModel = new ChatModel(chatModel.getId(), chatModel.getMessage(), chatModel.getChatType(), chatModel.getTime()
                , chatModel.getMsg_type(), true, chatModel.getCreatedDate(), getImageFilePath(chatModel.getId()), "",chatModel.getReplyBy());
        chatListModelList.set(position, chatListModel);
        adapter.notifyDataSetChanged();
    }

    public void notifyDownloadItemAudio(ChatModel chatModel, int position) {
        ChatModel chatListModel = new ChatModel(chatModel.getId(), chatModel.getMessage(), chatModel.getChatType(), chatModel.getTime()
                , chatModel.getMsg_type(), true, chatModel.getCreatedDate(), getAudioFilePath(chatModel.getId()), "","");
        chatListModelList.set(position, chatListModel);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode==Constant.ACTIVITY_RESULT_IMAGE_SHOP){
                try {
                    if (data != null) {
                        Uri uri = data.getData();
                        InputStream inputStream = activity.getContentResolver().openInputStream(uri);
                        String picturePath = Utility.getRealPathFromURIPath(uri,activity);
                        if (picturePath != null) {
                            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                            CheckFileExists(picturePath);
                        }
                    }

                } catch (Exception e) {
                    Utility.showToast(activity, getString(R.string.something_went_wrong), "");
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constant.permission_write_data) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(activity, "Tap Again", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == Constant.permission_Recording_audio) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(activity, "Tap Again", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void CheckFileExists(String filePath) {
        Log.d(TAG, "onActivityResult: " + filePath);
        File file = new File(filePath);
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            Log.d(TAG, "onActivityResult: " + filePath);
            Log.d(TAG, "onActivityResult: " + bitmap);
            showSendImageDialog(bitmap, filePath, file);
        } else {
            Utility.showToast(activity, getString(R.string.something_went_wrong), "");
        }
    }

    private void showSendImageDialog(Bitmap bitmap, String filePath, File file) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_send_image_dialog, null);
        LayoutSendImageDialogBinding sendImageDialogBinding = DataBindingUtil.bind(view);
        builder.setView(view);
        Dialog dialog = builder.create();
        if (dialog.getWindow() != null) {

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        assert sendImageDialogBinding != null;
        sendImageDialogBinding.shareImage.setImageBitmap(bitmap);
        sendImageDialogBinding.cancelShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        sendImageDialogBinding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                if (!filePath.isEmpty()) {
                    File file2 = new File(filePath);
                    long fileSize = file2.length() / (1024);
                    if (fileSize > 1024) {
                        Utility.showToastLatest(activity, "Max file size allowed is 1024 KB , Your file size is - " + fileSize + " KB", "ERROR");
                        return;
                    }
                }
                sendImage(filePath);
                postUserMessage("", filePath, "1");
            }
        });
        dialog.show();
    }

    private void sendImage(String filePath) {
        ChatModel chatModel = new ChatModel("", "", "1", "", "1", false, "", filePath, "local","");
        chatListModelList.add(chatModel);
        binding.recyclerView.smoothScrollToPosition((chatListModelList.size() - 1));
        adapter.notifyItemInserted((chatListModelList.size() - 1));
    }

    private void sendAudio(String filePath) {
        ChatModel chatModel = new ChatModel("", "", "reply", "", "3", false, "", filePath, "local","");
        chatListModelList.add(chatModel);
        binding.recyclerView.smoothScrollToPosition((chatListModelList.size() - 1));
        adapter.notifyItemInserted((chatListModelList.size() - 1));
    }

    public void onPause() {
        super.onPause();
        Log.d("Chatlist", "App paused");
        activityStopped = true;
        if (adapter!=null)
            adapter.onPause();
        stopRecording();
    }

    public void onResume() {
        super.onResume();
        Log.d("Chatlist", "App Resumed");
        activityStopped = false;
        handler();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            handler.removeCallbacks(runnable);
            if (adapter != null)
                adapter.onDestroy();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void parseShopOrderResponse(String response) {
        try {
            Log.d("ShopOrderResponse","response "+response);

            ShopOrderModel model= new Gson().fromJson(response, ShopOrderModel.class);
            if (model.getResCode().equals(Constant.CODE_200)) {

                contactNumber=model.getData().getContactNumber();
                shopName=model.getData().getShopName();
                lastActive=model.getData().getLastActive();
                image=model.getData().getImgUrl();

                if (model.getData().getOrderHistory()!=null) {
                    orderId=model.getData().getOrderHistory().get(0).getOrderId();
                    rating=model.getData().getOrderHistory().get(0).getRating();
                }

                /*binding.rating.setText(rating+" "+"Rating");*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package in.forpay.activity.supportchat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import in.forpay.R;
import in.forpay.adapter.supportchatAdapter.ChatListAdapter;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.ActivitySupportChatBinding;
import in.forpay.databinding.LayoutSendImageDialogBinding;
import in.forpay.model.supportchatModel.ChatListModel;
import in.forpay.model.supportchatModel.ChatModel;
import in.forpay.model.supportchatModel.MessageSendModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.PreferenceConnector;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;
import okhttp3.OkHttpClient;

public class SupportChatActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivitySupportChatBinding binding;
    private static final String TAG = "SupportChatActivity";
    private ProgressHelper progressHelper;
    private OkHttpClient client;
    private ArrayList<ChatListModel.DataBean> chatList;
    private ChatListAdapter adapter;
    private ArrayList<ChatModel> chatListModelList;
    private final int SELECT_IMAGE = 100;
    private Handler handler;
    private Runnable runnable;
    private File directory;
    volatile boolean activityStopped = false;
    private DatabaseHelper databaseHelper;
    PowerManager pm;
    private String call_type, call_number, call_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utility.checkLoggedUser(this);
        /* if(Utility.getToken(this).isEmpty()){
            Intent intent = new Intent(this,
                    LoginActivity1.class);
            startActivity(intent);
            finish();
        }*/

        binding = DataBindingUtil.setContentView(this, R.layout.activity_support_chat);
        binding.setActivity(this);
        databaseHelper = new DatabaseHelper(this);

        binding.backBtn.setOnClickListener(this);
        binding.btnSendMessage.setOnClickListener(this);
        binding.selectImage.setOnClickListener(this);

        directory = new File(Environment.getExternalStorageDirectory() + File.separator
                + getResources().getString(R.string.app_name) + File.separator + "SupportChat");

        client = new OkHttpClient.Builder()
                .connectTimeout(35, TimeUnit.SECONDS)
                .writeTimeout(35, TimeUnit.SECONDS)
                .readTimeout(35, TimeUnit.SECONDS)
                .build();
        chatList = new ArrayList<>();
        chatListModelList = new ArrayList<>();

        progressHelper = new ProgressHelper(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        binding.recChat.setLayoutManager(linearLayoutManager);
        adapter = new ChatListAdapter(SupportChatActivity.this, chatListModelList);
        binding.recChat.setAdapter(adapter);
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //progressHelper.show();
        getChatList();

        chatList.clear();
        try {
            chatList = databaseHelper.getChatSupportList();
        } catch (Exception e) {

        }
        createChatList();

        //init();

        makeDir();

        binding.callSupport.setOnClickListener(v -> onCallClick(call_type, call_number));

    }

    private void onCallClick(String call_type, String call_number) {

        if (call_type != null && call_number != null) {
            if (!(call_type.isEmpty() || call_number.isEmpty())) {
                //Toasty.success(SupportChatActivity.this,call_type+": "+call_number).show();
                if (call_type.equals("online")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + call_number));
                    startActivity(intent);
                } else {
                    Utility.showToastLatest(SupportChatActivity.this, call_message, "ERROR");
                }
            } else
                Utility.showToast(SupportChatActivity.this, call_message + "", "ERROR");
        } else {
            Utility.showToast(SupportChatActivity.this, call_message + "", "ERROR");
        }
    }

    private void init() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!activityStopped) {
                    boolean isScreenOn = true;
                    handler.postDelayed(this, 15000);
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                        if (pm != null) {
                            isScreenOn = pm.isInteractive();
                            Log.d("Chatlist", "Screen is " + isScreenOn);
                        }
                    }
                    if (isScreenOn) {
                        Log.d("Chatlist", "Screen on");
                        getChatList();
                    }
                }
            }
        };

        startListening();
//        handler.postDelayed(runnable, 15000);
    }

    private void chatListRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            //progressHelper.show();


            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_CHAT_LIST, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseChatListResponse(result, responseManager);

                            //Log.d("TEST","request - "+result);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void getChatList() {
        if (Utility.isNetworkConnected(this)) {

            Map<String, String> map1 = new HashMap<>();

            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("versionCode", Utility.getVersionCode(this)); // version code
            map1.put("os", Utility.getOs(this));
            String request = Utility.mapWrapper(this, map1);


            chatListRequest(request);


        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void parseChatListResponse(String response, ResponseManager responseManager) {

        try {
            Log.e("Chatlist", response);
            PreferenceConnector.writeString(this, PreferenceConnector.ACTIVE_KEY, response);


            ChatListModel chatListModel = new Gson().fromJson(response, ChatListModel.class);
            call_number = chatListModel.getSupportDetails().getMobile();
            call_message = chatListModel.getSupportDetails().getOnClickText();


//            Log.e("Chatlist","1-"+chatListModel.getResCode()+" - "+chatListModel.getData().size()+"respinse"+response);
            if (chatListModel.getResCode().equals(Constant.CODE_200)) {
                //Log.d("Chatlist",""+chatListModel.getResCode()+" - "+chatListModel.getData().size());
//                Log.e("model and list",chatListModel.getData().size()+" "+chatList.size());

//                if (chatListModel.getData().size() >= chatList.size()) {
                chatList.clear();

                chatList.addAll(chatListModel.getData());
                createChatList();

//                    Log.e("test", call_message+call_number);

                if (chatListModel.getSupportDetails().getStatus().equalsIgnoreCase("online")) {

                    Glide.with(this)
                            .load(this.getResources().getDrawable(R.drawable.ic_call_black_24dp))
                            .into(binding.callSupport);
                    binding.callSupport.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.greenNew), android.graphics.PorterDuff.Mode.SRC_IN);
                    call_type = "online";


                } else if (chatListModel.getSupportDetails().getStatus().equalsIgnoreCase("offline")) {
                    Glide.with(this)
                            .load(this.getResources().getDrawable(R.drawable.offline_call))
                            .into(binding.callSupport);
                    binding.callSupport.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                    call_type = "offline";
                } else if (chatListModel.getSupportDetails().getStatus().equalsIgnoreCase("busy")) {
                    binding.callSupport.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.purple_call), android.graphics.PorterDuff.Mode.SRC_IN);
                    call_type = "busy";
                } else if (chatListModel.getSupportDetails().getStatus().equalsIgnoreCase("away")) {
                    Glide.with(this)
                            .load(this.getResources().getDrawable(R.drawable.online_call))
                            .into(binding.callSupport);
                    binding.callSupport.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.orange_new), android.graphics.PorterDuff.Mode.SRC_IN);
                    call_type = "away";
                } else {
                    binding.callSupport.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.orange_new), android.graphics.PorterDuff.Mode.SRC_IN);
                    call_type = "away";
                }

                addChatImageId(chatListModel.getData());
//                }

                if (chatListModel.getSupportName() != null) {
                    binding.supportTitle.setText(chatListModel.getSupportName());
                }
                if (chatListModel.getSupportActiveTime() != null) {
                    binding.supportActiveTime.setText(chatListModel.getSupportActiveTime());
                }

                try {
                    chatList = databaseHelper.getChatSupportList();
                } catch (Exception e) {

                }

            } else {
                // Utility.showToastLatest(SupportChatActivity.this, chatListModel.getResText(), "ERROR");
                Glide.with(this)
                        .load(this.getResources().getDrawable(R.drawable.offline_call))
                        .into(binding.callSupport);
                binding.callSupport.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                call_type = "offline";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createChatList() {
        chatListModelList.clear();
        for (ChatListModel.DataBean dataBean : chatList) {
            if (dataBean.getMsgType().equals("0")) {
                ChatModel chatListModel = new ChatModel(dataBean.getId(), dataBean.getChatMsg(), dataBean.getChatType(), dataBean.getTime()
                        , dataBean.getMsgType(), true, dataBean.getCreatedDate(), "", "", dataBean.getReplyBy());
                chatListModelList.add(chatListModel);
            } else {
                ChatModel chatListModel = new ChatModel(dataBean.getId(), dataBean.getChatMsg(), dataBean.getChatType(), dataBean.getTime()
                        , dataBean.getMsgType(), true, dataBean.getCreatedDate(), getImageFilePath(dataBean.getId()), "", dataBean.getReplyBy());
                chatListModelList.add(chatListModel);
            }
        }
        setAdapter(chatListModelList);
    }

    private void setAdapter(ArrayList<ChatModel> chatListModelList) {
        try {
            progressHelper.dismiss();
            binding.recChat.smoothScrollToPosition((chatListModelList.size() - 1));
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                onBackPressed();
                break;

            case R.id.btn_send_message:
                if (binding.edtMessage.getText().toString().trim().isEmpty()) {
                    Utility.showToast(this, "Please type message.", "");
                } else {
                    sendMessage();
                    postUserMessage(binding.edtMessage.getText().toString(), "", "0");
                    binding.edtMessage.getText().clear();
                }
                break;

            case R.id.select_image:
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    check();
                    return;
                }
                imagePickerIntent();
                break;

        }
    }

    private void sendMessage() {
        ChatModel chatModel = new ChatModel("", binding.edtMessage.getText().toString(), "1", ""
                , "0", false, "", "", "local", "");
        chatListModelList.add(chatModel);
        binding.recChat.smoothScrollToPosition((chatListModelList.size() - 1));
        adapter.notifyDataSetChanged();
    }

    /**
     * Send Message Api Call
     */


    private void postUserMessageResponse(String response, ResponseManager responseManager, String filePath) {

        startListening();
        try {
            File file = null;


            if (!filePath.isEmpty()) {
                file = new File(filePath);
                long fileSize = file.length() / (1024);
                //Log.d("Chatlist","size -"+fileSize);

            }

            MessageSendModel messageSendModel = new Gson().fromJson(response, MessageSendModel.class);
            if (messageSendModel.getResCode().equals(Constant.CODE_200)) {
                if (file != null) {

                    prepareCopyFile(messageSendModel.getData().get(0).getId(), file);
                }
                notifyMessage(messageSendModel, filePath);
            } else {
                Utility.showToast(SupportChatActivity.this, messageSendModel.getResText(), "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void postUserMessageRequest(String request, String filePath, String msgType) {
        if (Utility.isNetworkConnected(this)) {

            if (!filePath.isEmpty()) {
                QueryManager.getInstance().filePath = filePath;
                QueryManager.getInstance().fileType = "Image";
                //file = new File(filePath);
                //fileName = file.getName();
            }

            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_CHAT_POSTMESSAGE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            postUserMessageResponse(result, responseManager, filePath);
                            //Log.d("TEST","request - "+result);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    /**
     * Add chat image id & message in local db
     */
    private void addChatImageId(List<ChatListModel.DataBean> data) {
        if (data == null) {
            return;
        }
        // add imageId & message in local db

        for (int i = 0; i < data.size(); i++) {
            try {
                //if data already present then update data
                databaseHelper.insertChatSupportHistory(data.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void postUserMessage(String message, String filePath, String msg_type) {
        if (Utility.isNetworkConnected(this)) {
            stopListening();
            String msg = message;
            msg = escapeJavaString(msg);


            Map<String, String> map1 = new HashMap<>();

            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("versionCode", Utility.getVersionCode(this)); // version code
            map1.put("os", Utility.getOs(this));
            map1.put("message", msg);
            map1.put("type", msg_type);
            String request = Utility.mapWrapper(this, map1);


            File file = null;

            if (!filePath.isEmpty()) {
                file = new File(filePath);
                long fileSize = file.length() / (1024);
                if (fileSize > 1024) {
                    Utility.showToastLatest(this, "Max file size allowed is 1024 KB , Your file size is - " + fileSize + " KB", "ERROR");
                    return;
                }
            }

            postUserMessageRequest(request, filePath, msg_type);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void notifyMessage(MessageSendModel messageSendModel, String filePath) {
        if (messageSendModel.getData().get(0).getMsgType().equals("0")) {
            ChatModel chatModel = new ChatModel(messageSendModel.getData().get(0).getId(), messageSendModel.getData().get(0).getChatMsg(), messageSendModel.getData().get(0).getChatType(), messageSendModel.getData().get(0).getTime()
                    , messageSendModel.getData().get(0).getMsgType(), true, "", "", "", "");
            chatListModelList.set((chatListModelList.size() - 1), chatModel);
            adapter.notifyDataSetChanged();
        } else {
            ChatModel chatModel = new ChatModel(messageSendModel.getData().get(0).getId(), messageSendModel.getData().get(0).getMsg(), messageSendModel.getData().get(0).getChatType(), messageSendModel.getData().get(0).getTime()
                    , messageSendModel.getData().get(0).getMsgType(), true, "", filePath, "", "");
            chatListModelList.set((chatListModelList.size() - 1), chatModel);
            adapter.notifyDataSetChanged();
        }
    }

    private void imagePickerIntent() {
        Intent imageSelectIntent = new Intent(Intent.ACTION_PICK);
        imageSelectIntent.setType("image/*");
        startActivityForResult(imageSelectIntent, SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECT_IMAGE) {
            try {
                if (data != null) {
                    Uri uri = data.getData();
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    String picturePath = getPath(uri);
                    if (picturePath != null) {
                        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                        CheckFileExists(picturePath);
                    }
                }

            } catch (Exception e) {
                Utility.showToast(SupportChatActivity.this, getString(R.string.something_went_wrong), "");
            }
        }
    }

    public String getPath(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(uri,
                filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            return picturePath;
        } else {
            return null;
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
            Utility.showToast(SupportChatActivity.this, getString(R.string.something_went_wrong), "");
        }
    }

    private void showSendImageDialog(Bitmap bitmap, String filePath, File file) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_send_image_dialog, null);
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
                        Utility.showToastLatest(SupportChatActivity.this, "Max file size allowed is 1024 KB , Your file size is - " + fileSize + " KB", "ERROR");
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
        ChatModel chatModel = new ChatModel("", "", "1", "", "1", false, "", filePath, "local", "");
        chatListModelList.add(chatModel);
        binding.recChat.smoothScrollToPosition((chatListModelList.size() - 1));
        adapter.notifyItemInserted((chatListModelList.size() - 1));
    }

    public void check() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            //requestStoragePermission();
            checkPermission();
        }
    }

    private void checkPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                imagePickerIntent();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\n" +
                        "Please turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null)
            if (runnable != null)
                handler.removeCallbacks(runnable);
    }

    private void prepareCopyFile(String id, File finalFile) {
        try {
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String destfileName = directory + File.separator + id + ".png";
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
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (!directory.exists()) {
                directory.mkdirs();
            }
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

    public void notifyDownloadItem(ChatModel chatModel, int position) {
        ChatModel chatListModel = new ChatModel(chatModel.getId(), chatModel.getMessage(), chatModel.getChatType(), chatModel.getTime()
                , chatModel.getMsg_type(), true, chatModel.getCreatedDate(), getImageFilePath(chatModel.getId()), "", chatModel.getReplyBy());
        chatListModelList.set(position, chatListModel);
        adapter.notifyDataSetChanged();
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

    public String escapeJavaString2(String st) {
        StringBuilder builder = new StringBuilder();
        try {
            for (int i = 0; i < st.length(); i++) {
                char c = st.charAt(i);
                if (!Character.isLetterOrDigit(c) && !Character.isSpaceChar(c) && !Character.isWhitespace(c)) {
                    String unicode = String.valueOf(c);
                    int code = (int) c;
                    if (!(code >= 0 && code <= 255)) {
                        unicode = "\\\\u" + Integer.toHexString(c);
                    }
                    builder.append(unicode);
                } else {
                    builder.append(c);
                }
            }
            Log.i("Unicode Block", builder.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return builder.toString();
    }

    public void onPause() {
        super.onPause();
        Log.d("Chatlist", "App paused");
        activityStopped = true;
    }

    public void onResume() {
        super.onResume();
        Log.d("Chatlist", "App Resumed");
        activityStopped = false;
        init();
    }

    void startListening() {
        runnable.run();
    }

    void stopListening() {
        handler.removeCallbacks(runnable);
    }

}

package in.forpay.adapter.shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import in.forpay.R;
import in.forpay.activity.supportchat.ImageDetailActivity;
import in.forpay.databinding.AdapterShopChatAudioMyBinding;
import in.forpay.databinding.AdapterShopChatAudioOtherBinding;
import in.forpay.databinding.AdapterShopChatListImgBinding;
import in.forpay.databinding.AdapterShopChatMyBinding;
import in.forpay.databinding.AdapterShopChatOtherBinding;
import in.forpay.listener.ChatImageListener;
import in.forpay.model.supportchatModel.ChatModel;
import in.forpay.util.Constant;

public class ShopChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Handler.Callback{

    private final File audioDirectory;
    private final String audioPath;
    private Context context;
    private ArrayList<ChatModel> chatList;
    private final int MSG_TYPE_TEXT_RESPONSE = 0;
    private final int MSG_TYPE_TEXT_REPLAY = 1;
    private final int MSG_TYPE_IMAGE = 2;
    private final int MSG_TYPE_AUDIO_RESPONSE = 3;
    private final int MSG_TYPE_AUDIO_REPLAY = 4;
    private File directory;
    private String dirPath;
    private static final int MSG_UPDATE_SEEK_BAR = 1845;
    private MediaPlayer mediaPlayer;
    private Handler uiUpdateHandler;
    private int playingPosition;
    private RecyclerView.ViewHolder holder;
    private String playbackStr="";
    private ChatImageListener listener;

    public ShopChatListAdapter(Context context, ArrayList<ChatModel> chatList, ChatImageListener listener) {
        this.context = context;
        this.chatList = chatList;
        this.listener = listener;

        dirPath = Environment.getExternalStorageDirectory() + File.separator
                + context.getResources().getString(R.string.app_name) + File.separator + Constant.SHOP_DIRECTORY_NAME;
        directory = new File(dirPath);

        audioPath = Environment.getExternalStorageDirectory() + File.separator
                + context.getResources().getString(R.string.app_name) + File.separator + Constant.SHOP_DIRECTORY_NAME + File.separator +Constant.SHOP_AUDIO_DIRECTORY_NAME ;
        audioDirectory=new File(audioPath);

        this.playingPosition = -1;
        uiUpdateHandler = new Handler(this);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_TEXT_REPLAY) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_shop_chat_my, parent, false);
            return new ViewHolderReplay(view);
        } else if (viewType == MSG_TYPE_TEXT_RESPONSE) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_shop_chat_other, parent, false);
            return new ViewHolderResponse(view);
        } else if (viewType == MSG_TYPE_IMAGE){
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_shop_chat_list_img, parent, false);
            return new ViewHolderImage(view);
        }else if (viewType == MSG_TYPE_AUDIO_REPLAY){
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_shop_chat_audio_my, parent, false);
            return new ViewHolderReplayAudio(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_shop_chat_audio_other, parent, false);
            return new ViewHolderResponseAudio(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == MSG_TYPE_TEXT_REPLAY) {
            ViewHolderReplay viewHolder = (ViewHolderReplay) holder;

            if (chatList.get(position).isMsgDelivered()) {
                viewHolder.binding.isMsgSent.setVisibility(View.VISIBLE);
            } else {
                viewHolder.binding.isMsgSent.setVisibility(View.GONE);
            }
            viewHolder.binding.txtMessage.setText(unescapeJava(chatList.get(position).getMessage()));
            viewHolder.binding.msgTime.setText(Constant.getTimeDateFormat(chatList.get(position).getCreatedDate()));
        }else if(viewType == MSG_TYPE_TEXT_RESPONSE){
            ViewHolderResponse viewHolder = (ViewHolderResponse) holder;

            viewHolder.binding.isMsgSent.setVisibility(View.GONE);
            viewHolder.binding.txtMessage.setText(unescapeJava(chatList.get(position).getMessage()));
            viewHolder.binding.msgTime.setText(Constant.getTimeDateFormat(chatList.get(position).getCreatedDate()));

        }else if (viewType==MSG_TYPE_AUDIO_REPLAY){
            ViewHolderReplayAudio viewHolder = (ViewHolderReplayAudio) holder;
            if (chatList.get(position).isMsgDelivered()) {
                viewHolder.binding.isMsgSent.setVisibility(View.VISIBLE);
            } else {
                viewHolder.binding.isMsgSent.setVisibility(View.GONE);
            }
            viewHolder.binding.msgTime.setText(Constant.getTimeDateFormat(chatList.get(position).getCreatedDate()));

            if (position == playingPosition) {
                this.holder = holder;
                // this view holder corresponds to the currently playing audio cell
                // update its view to show playing progress
                updatePlayingViewReplay();
            } else {
                // and this one corresponds to non playing
                updateNonPlayingViewReplay(holder);
            }

        }else if (viewType==MSG_TYPE_AUDIO_RESPONSE){
            ViewHolderResponseAudio viewHolder = (ViewHolderResponseAudio) holder;
            viewHolder.binding.msgTime.setText(Constant.getTimeDateFormat(chatList.get(position).getCreatedDate()));

            if (position == playingPosition) {
                this.holder = holder;
                // this view holder corresponds to the currently playing audio cell
                // update its view to show playing progress
                updatePlayingView();
            } else {
                // and this one corresponds to non playing
                updateNonPlayingView(holder);
            }
        }
        else {
            ViewHolderImage viewHolderImage = (ViewHolderImage) holder;
            if (chatList.get(position).getChatType().equals("reply")) {
                viewHolderImage.binding.chatRootViewImg.setGravity(Gravity.END);
                viewHolderImage.binding.background.setBackgroundColor(ContextCompat.getColor(context,R.color.gray_shop));
                if (chatList.get(position).isMsgDelivered()) {
                    viewHolderImage.binding.isMsgSent.setVisibility(View.VISIBLE);
                } else {
                    viewHolderImage.binding.isMsgSent.setVisibility(View.GONE);
                }

            } else {
                viewHolderImage.binding.chatRootViewImg.setGravity(Gravity.START);
                viewHolderImage.binding.background.setBackgroundColor(ContextCompat.getColor(context,R.color.purple));
                viewHolderImage.binding.isMsgSent.setVisibility(View.GONE);
            }

            if (!chatList.get(position).isMsgDelivered()) {
                showImageFromFilePath(getImageFilePath(chatList.get(position).getFilePath()), viewHolderImage.binding.chatImage);
            }

            if (chatList.get(position).isMsgDelivered()){
                if (getImageFilePath(chatList.get(position).getId()).isEmpty()) {
                    Glide.with(context).load(chatList.get(position).getMessage())
                            .placeholder(R.drawable.chat_placeholder).into(viewHolderImage.binding.chatImage);
                    downloadImage(position, viewHolderImage);
                } else {
                    showImageFromFilePath(getImageFilePath(chatList.get(position).getId()), viewHolderImage.binding.chatImage);
                }
            }
            viewHolderImage.binding.msgTime.setText(Constant.getTimeDateFormat(chatList.get(position).getCreatedDate()));
            //viewHolderImage.binding.msgTime.setText(chatList.get(position).getCreatedDate());

            viewHolderImage.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (chatList.get(position).getFilePath().isEmpty()) {
                        downloadImage(position, viewHolderImage);
                    } else {
                        Intent intent = new Intent(context, ImageDetailActivity.class);
                        intent.putExtra("img_url", chatList.get(position).getFilePath());
                        intent.putExtra(Constant.IS_FROM_SHOP_CHAT,true);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    private void updatePlayingView() {
        if (holder instanceof ViewHolderResponseAudio) {
            ViewHolderResponseAudio viewHolder = (ViewHolderResponseAudio) holder;
            viewHolder.binding.seekBar.setMax(mediaPlayer.getDuration());
            viewHolder.binding.seekBar.setProgress(mediaPlayer.getCurrentPosition());
            viewHolder.binding.seekBar.setEnabled(true);
            viewHolder.binding.totalTime.setText(playbackStr);
            if (mediaPlayer.isPlaying()) {
                uiUpdateHandler.sendEmptyMessageDelayed(MSG_UPDATE_SEEK_BAR, 100);
                viewHolder.binding.playBtn.setImageResource(R.drawable.pause_icon);
            } else {
                uiUpdateHandler.removeMessages(MSG_UPDATE_SEEK_BAR);
                viewHolder.binding.playBtn.setImageResource(R.drawable.play_icon);
            }
        }
    }
    private void updatePlayingViewReplay() {
        if (holder instanceof ViewHolderReplayAudio) {
            ViewHolderReplayAudio viewHolder = (ViewHolderReplayAudio) holder;
            viewHolder.binding.seekBar.setMax(mediaPlayer.getDuration());
            viewHolder.binding.seekBar.setProgress(mediaPlayer.getCurrentPosition());
            viewHolder.binding.seekBar.setEnabled(true);
            viewHolder.binding.totalTime.setText(playbackStr);
            if (mediaPlayer.isPlaying()) {
                uiUpdateHandler.sendEmptyMessageDelayed(MSG_UPDATE_SEEK_BAR, 100);
                viewHolder.binding.playBtn.setImageResource(R.drawable.pause_icon);
            } else {
                uiUpdateHandler.removeMessages(MSG_UPDATE_SEEK_BAR);
                viewHolder.binding.playBtn.setImageResource(R.drawable.play_icon);
            }
        }
    }

    private void updateNonPlayingView(RecyclerView.ViewHolder holder) {
        if (holder instanceof ViewHolderResponseAudio) {
            if (holder == this.holder) {
                uiUpdateHandler.removeMessages(MSG_UPDATE_SEEK_BAR);
            }
            ViewHolderResponseAudio viewHolder1 = (ViewHolderResponseAudio) holder;
            viewHolder1.binding.seekBar.setEnabled(false);
            viewHolder1.binding.seekBar.setProgress(0);
            viewHolder1.binding.playBtn.setImageResource(R.drawable.play_icon);
            viewHolder1.binding.totalTime.setText("");
        }
    }

    private void updateNonPlayingViewReplay(RecyclerView.ViewHolder holder) {
        if (holder instanceof ViewHolderReplayAudio) {
            if (holder == this.holder) {
                uiUpdateHandler.removeMessages(MSG_UPDATE_SEEK_BAR);
            }
            ViewHolderReplayAudio viewHolder1 = (ViewHolderReplayAudio) holder;
            viewHolder1.binding.seekBar.setEnabled(false);
            viewHolder1.binding.seekBar.setProgress(0);
            viewHolder1.binding.playBtn.setImageResource(R.drawable.play_icon);
            viewHolder1.binding.totalTime.setText("");
        }
    }

    void stopPlayer() {
        if (null != mediaPlayer) {
            releaseMediaPlayer();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_SEEK_BAR: {
                if (mediaPlayer!=null) {
                    if (holder instanceof ViewHolderResponseAudio) {
                        ViewHolderResponseAudio viewHolder = (ViewHolderResponseAudio) holder;
                        viewHolder.binding.seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    } else {
                        ViewHolderReplayAudio viewHolder = (ViewHolderReplayAudio) holder;
                        viewHolder.binding.seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                    uiUpdateHandler.sendEmptyMessageDelayed(MSG_UPDATE_SEEK_BAR, 100);
                    updateTime(mediaPlayer.getCurrentPosition());
                }
                return true;
            }
        }
        return false;
    }

    private void updateTime(int currentTime){
        playbackStr = new String();
        // set the current time
        // its ok to show 00:00 in the UI
        playbackStr=(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) currentTime), TimeUnit.MILLISECONDS.toSeconds((long) currentTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) currentTime))));
        if (holder instanceof ViewHolderResponseAudio){
            ViewHolderResponseAudio viewHolder = (ViewHolderResponseAudio) holder;
            viewHolder.binding.totalTime.setText(playbackStr);
        }else {
            ViewHolderReplayAudio viewHolder = (ViewHolderReplayAudio) holder;
            viewHolder.binding.totalTime.setText(playbackStr);
        }
    }
    /**
     * Changes the view to playing state
     * - icon is changed to pause
     * - seek bar enabled
     * - start seek bar updater, if needed
     */

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        try {
            if (playingPosition == holder.getAdapterPosition()) {
                // view holder displaying playing audio cell is being recycled
                // change its state to non-playing
                if (holder instanceof ViewHolderResponseAudio) {
                    ViewHolderResponseAudio viewHolder = (ViewHolderResponseAudio) this.holder;
                    updateNonPlayingView(viewHolder);
                } else if (holder instanceof ViewHolderReplayAudio) {
                    ViewHolderReplayAudio viewHolder = (ViewHolderReplayAudio) this.holder;
                    updateNonPlayingView(viewHolder);
                }
                //holder = null;
            }
        }catch (Exception e){
            //Log.e("onViewRecycled",e.getLocalizedMessage()+"--->"+ e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (chatList.get(position).getMsg_type().equals("0")) {
            if (chatList.get(position).getChatType().equals("reply"))
                return MSG_TYPE_TEXT_REPLAY;
            else
                return MSG_TYPE_TEXT_RESPONSE;
        } else if (chatList.get(position).getMsg_type().equals("1")){
            return MSG_TYPE_IMAGE;
        }else {
            if (chatList.get(position).getChatType().equals("reply"))
                return MSG_TYPE_AUDIO_REPLAY;
            else
                return MSG_TYPE_AUDIO_RESPONSE;

        }
    }

    public static class ViewHolderReplay extends RecyclerView.ViewHolder {
        AdapterShopChatMyBinding binding;

        public ViewHolderReplay(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
    public static class ViewHolderResponse extends RecyclerView.ViewHolder {
        AdapterShopChatOtherBinding binding;

        public ViewHolderResponse(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
    public static class ViewHolderImage extends RecyclerView.ViewHolder {
        AdapterShopChatListImgBinding binding;

        public ViewHolderImage(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public class ViewHolderReplayAudio extends RecyclerView.ViewHolder implements SeekBar.OnSeekBarChangeListener{
        AdapterShopChatAudioMyBinding binding;

        public ViewHolderReplayAudio(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.seekBar.setOnSeekBarChangeListener(this);

            binding.playBtn.setOnClickListener(v->{
                if (holder instanceof ViewHolderResponseAudio || getAdapterPosition() != playingPosition) {
                    releaseMediaPlayer();
                }
                holder = this;
                if (getAudioFilePath(chatList.get(getAdapterPosition()).getId()).isEmpty()) {
                    download_audio(binding.pBar,getAdapterPosition());
                } else {
                    playAudioReplay(getAdapterPosition());
                }
            });
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                mediaPlayer.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }

    public class ViewHolderResponseAudio extends RecyclerView.ViewHolder implements SeekBar.OnSeekBarChangeListener {
        AdapterShopChatAudioOtherBinding binding;

        public ViewHolderResponseAudio(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.seekBar.setOnSeekBarChangeListener(this);

             binding.playBtn.setOnClickListener(v->{
                 if (holder instanceof ViewHolderReplayAudio || getAdapterPosition() != playingPosition) {
                     releaseMediaPlayer();
                 }
                 holder = this;
                if (getAudioFilePath(chatList.get(getAdapterPosition()).getId()).isEmpty()) {
                    download_audio(binding.pBar,getAdapterPosition());
                } else {
                    playAudioResponse(getAdapterPosition());
                }
            });
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                mediaPlayer.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }

    private void playAudioResponse(int adapterPosition) {
        if (holder instanceof ViewHolderResponseAudio) {
            if (adapterPosition == playingPosition) {
                // toggle between play/pause of audio
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                }
            } else {
                // start another audio playback
                playingPosition = adapterPosition;
                ViewHolderResponseAudio viewHolder = (ViewHolderResponseAudio) holder;
                if (mediaPlayer != null) {
                    updateNonPlayingView(holder);
                    mediaPlayer.release();
                }
                //holder = this;
                startMediaPlayer(getAudioFilePath(chatList.get(adapterPosition).getId()));
            }
            updatePlayingView();
        }else {
            playAudioReplay(adapterPosition);
        }
    }

    private void playAudioReplay(int adapterPosition) {
        if (holder instanceof ViewHolderReplayAudio) {
            if (adapterPosition == playingPosition) {
                // toggle between play/pause of audio
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                }
            } else {
                // start another audio playback
                playingPosition = adapterPosition;

                ViewHolderReplayAudio viewHolder = (ViewHolderReplayAudio) holder;
                if (mediaPlayer != null) {
                    updateNonPlayingViewReplay(holder);
                    mediaPlayer.release();
                }
                //holder = this;
                startMediaPlayer(getAudioFilePath(chatList.get(adapterPosition).getId()));

            }
            updatePlayingViewReplay();
        }else {
            playAudioResponse(adapterPosition);
        }
    }

    private void startMediaPlayer(String audioPath) {
        try {
            mediaPlayer = MediaPlayer.create(context.getApplicationContext(), Uri.parse(audioPath));
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    releaseMediaPlayer();
                }
            });
            mediaPlayer.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void releaseMediaPlayer() {
        if (holder instanceof ViewHolderResponseAudio){
            ViewHolderResponseAudio viewHolder = (ViewHolderResponseAudio) holder;
            updateNonPlayingView(viewHolder);
        }else {
            ViewHolderReplayAudio viewHolder = (ViewHolderReplayAudio) holder;
            if (null != viewHolder) {
                updateNonPlayingViewReplay(viewHolder);
            }
        }
        if (mediaPlayer!=null) {
            mediaPlayer.release();
            mediaPlayer = null;
            playingPosition = -1;
        }
    }

    private void showImageFromFilePath(String filePath, ImageView imageView) {
        try {
            File imgFile = new File(filePath);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageView.setImageBitmap(myBitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadImage(int position, ViewHolderImage viewHolderImage) {
        try {
            if (!directory.exists()) {
                directory.mkdirs();
            }

            PRDownloader.download(chatList.get(position).getMessage(), dirPath, (chatList.get(position).getId() + ".png"))
                    .build().setOnProgressListener(new OnProgressListener() {
                @Override
                public void onProgress(Progress progress) {
                    Log.d("DownloadImg", "onProgress: " + progress.currentBytes);
                }
            }).setOnPauseListener(new OnPauseListener() {
                @Override
                public void onPause() {
                    Log.d("DownloadImg", "onPause: ");
                }
            }).setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel() {
                    Log.d("DownloadImg", "onCancel: ");
                }
            }).setOnStartOrResumeListener(new OnStartOrResumeListener() {
                @Override
                public void onStartOrResume() {
                    Log.d("DownloadImg", "onStartOrResume: ");
                }
            }).start(new OnDownloadListener() {
                @Override
                public void onDownloadComplete() {
                    Log.d("DownloadImg", "Download Complete POSITION : " + position);
                    try {
                        listener.notifyDownloadItemImage(chatList.get(position), position);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Error error) {
                    Log.d("DownloadImg", "onError: " + error.getServerErrorMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void download_audio(final ProgressBar p_bar, int position){
        if (!audioDirectory.exists()){
            audioDirectory.mkdirs();
        }
        p_bar.setVisibility(View.VISIBLE);
        int downloadId = PRDownloader.download(chatList.get(position).getMessage(), audioPath, (chatList.get(position).getId() + ".mp3"))
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        p_bar.setVisibility(View.GONE);
                        //((ShopListChatActivity) context).notifyDownloadItemAudio(chatList.get(position), position);
                        String filePath = audioDirectory + File.separator + chatList.get(position).getId() + ".mp3";
                        playAudioResponse(position);
                    }

                    @Override
                    public void onError(Error error) {

                    }
                });
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

    public String unescapeJava(String escaped) {
        String processed = "";

        try {
            if (escaped.indexOf("\\u") == -1)
                return escaped;

            int position = escaped.indexOf("\\u");
            while (position != -1) {
                if (position != 0)
                    processed += escaped.substring(0, position);
                String token = escaped.substring(position + 2, position + 6);
                escaped = escaped.substring(position + 6);
                processed += (char) Integer.parseInt(token, 16);
                /*if (Character.UnicodeBlock.of((char) Integer.parseInt(token, 16)) != Character.UnicodeBlock.EMOTICONS) {
                    escaped = escaped.substring(position + 6);
                    processed += (char) Integer.parseInt(token, 16);
                } else {
                    String token1 = escaped.substring(position + 2, position + 5);
                    escaped = escaped.substring(position + 5);
                    processed += (char) Integer.parseInt(token1, 16);
                }*/
                position = escaped.indexOf("\\u");
            }
            processed += escaped;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return processed;
    }
    public String unescapeJava2(String escaped) {
        String processed = "";

        try {
            if (escaped.indexOf("\\u") == -1)
                return escaped;

            int position = escaped.indexOf("\\u");
            while (position != -1) {
                if (position != 0)
                    processed += escaped.substring(0, position);
                String token = escaped.substring(position + 2, position + 5);
                if (Character.UnicodeBlock.of((char) Integer.parseInt(token, 16)) == Character.UnicodeBlock.DEVANAGARI){
                    escaped = escaped.substring(position + 5);
                    processed += (char) Integer.parseInt(token, 16);
                } else {
                    String token1 = escaped.substring(position + 2, position + 6);
                    escaped = escaped.substring(position + 6);
                    processed += (char) Integer.parseInt(token1, 16);
                }
                position = escaped.indexOf("\\u");
            }
            processed += escaped;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return processed;
    }

    public void onDestroy() {
        releaseMediaPlayer();
    }

    public void onPause(){
        stopPlayer();
    }
}


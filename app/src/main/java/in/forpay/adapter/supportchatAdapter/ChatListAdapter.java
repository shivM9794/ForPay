package in.forpay.adapter.supportchatAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
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

import in.forpay.R;
import in.forpay.activity.supportchat.ImageDetailActivity;
import in.forpay.activity.supportchat.SupportChatActivity;
import in.forpay.databinding.AdapterChatListBinding;
import in.forpay.databinding.AdapterChatListImgBinding;
import in.forpay.model.supportchatModel.ChatModel;
import in.forpay.util.Constant;

public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ChatModel> chatList;
    private final int MSG_TYPE_TEXT = 0;
    private final int MSG_TYPE_IMAGE = 1;
    private File directory;
    private String dirPath;

    public ChatListAdapter(Context context, ArrayList<ChatModel> chatList) {
        this.context = context;
        this.chatList = chatList;

        dirPath = Environment.getExternalStorageDirectory() + File.separator
                + context.getResources().getString(R.string.app_name) + File.separator + "SupportChat";
        directory = new File(dirPath);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_TEXT) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_chat_list, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_chat_list_img, parent, false);
            return new ViewHolderImage(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == MSG_TYPE_TEXT) {
            ViewHolder viewHolder = (ViewHolder) holder;
            if (chatList.get(position).getChatType().equals("1")) {
                viewHolder.binding.chatRootView.setGravity(Gravity.END);
                viewHolder.binding.txtMessage.setBackground(context.getResources().getDrawable(R.drawable.msg_user_bg));
                if (chatList.get(position).isMsgDelivered()) {
                    viewHolder.binding.isMsgSent.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.binding.isMsgSent.setVisibility(View.GONE);
                    if (chatList.get(position).getPostType().equals("local")) {
                        viewHolder.binding.isMsgSent.setBackgroundResource(R.drawable.ic_msg_sent);
                        viewHolder.binding.isMsgSent.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_msg_sent));
                        viewHolder.binding.isMsgSent.setVisibility(View.VISIBLE);
                    }


                }
                viewHolder.binding.msgTime.setText(Constant.getTimeDateFormat(chatList.get(position).getCreatedDate()));

            } else {

                viewHolder.binding.chatRootView.setGravity(Gravity.START);
                viewHolder.binding.isMsgSent.setVisibility(View.GONE);
                viewHolder.binding.txtMessage.setBackground(context.getResources().getDrawable(R.drawable.msg_admin_bg));
                String time = Constant.getTimeDateFormat(chatList.get(position).getCreatedDate()) + " | " + chatList.get(position).getReplyBy();

                viewHolder.binding.msgTime.setText(time);


            }


            viewHolder.binding.txtMessage.setText(unescapeJava(chatList.get(position).getMessage()));
            //viewHolder.binding.msgTime.setText(chatList.get(position).getCreatedDate());
        } else {
            ViewHolderImage viewHolderImage = (ViewHolderImage) holder;
            if (chatList.get(position).getChatType().equals("1")) {
                viewHolderImage.binding.chatRootViewImg.setGravity(Gravity.END);
                if (chatList.get(position).isMsgDelivered()) {
                    viewHolderImage.binding.isMsgSent.setVisibility(View.VISIBLE);
                } else {
                    viewHolderImage.binding.isMsgSent.setVisibility(View.GONE);

                }

            } else {
                viewHolderImage.binding.chatRootViewImg.setGravity(Gravity.START);
                viewHolderImage.binding.isMsgSent.setVisibility(View.GONE);
            }

            if (!chatList.get(position).isMsgDelivered()) {
                showImageFromFilePath(getImageFilePath(chatList.get(position).getFilePath()), viewHolderImage.binding.chatImage);
            }

            if (chatList.get(position).isMsgDelivered()) {
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
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatList.get(position).getMsg_type().equals("0")) {
            return MSG_TYPE_TEXT;
        } else {
            return MSG_TYPE_IMAGE;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AdapterChatListBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public static class ViewHolderImage extends RecyclerView.ViewHolder {
        AdapterChatListImgBinding binding;

        public ViewHolderImage(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
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
                    ((SupportChatActivity) context).notifyDownloadItem(chatList.get(position), position);
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

    private String getImageFilePath(String id) {
        String filePath = directory + File.separator + id + ".png";
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
                if (Character.UnicodeBlock.of((char) Integer.parseInt(token, 16)) == Character.UnicodeBlock.DEVANAGARI) {
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
}

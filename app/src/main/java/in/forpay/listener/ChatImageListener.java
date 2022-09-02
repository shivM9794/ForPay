package in.forpay.listener;

import in.forpay.model.supportchatModel.ChatModel;

public interface ChatImageListener {

    void notifyDownloadItemImage(ChatModel chatModel, int position);
}

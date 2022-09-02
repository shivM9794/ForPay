package in.forpay.model.supportchatModel;

public class ChatModel {

    private String id;
    private String message;
    private String chatType;
    private String time;
    private String msg_type;
    private boolean isMsgDelivered;
    private int msgPosition;
    private String filePath;
    private String createdDate;
    private String postType;
    private String replyBy;

    public ChatModel() {
    }


    public ChatModel(String id,String message,String chatType, String time, String msg_type, boolean isMsgDelivered,String createdDate,String filePath,String postType,String replyBy) {
        this.id = id;
        this.message = message;
        this.chatType = chatType;
        this.time = time;
        this.msg_type = msg_type;
        this.isMsgDelivered = isMsgDelivered;
        this.filePath = filePath;
        this.createdDate = createdDate;
        this.postType=postType;
        this.replyBy = replyBy;

    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostType() {
        return postType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public boolean isMsgDelivered() {
        return isMsgDelivered;
    }

    public void setMsgDelivered(boolean msgDelivered) {
        isMsgDelivered = msgDelivered;
    }

    public int getMsgPosition() {
        return msgPosition;
    }

    public void setMsgPosition(int msgPosition) {
        this.msgPosition = msgPosition;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getReplyBy() {
        return replyBy;
    }

    public void setReplyBy(String replyBy) {
        this.replyBy = replyBy;
    }
}

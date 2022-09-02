package in.forpay.model.supportchatModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MessageSendModel implements Serializable {

    private String resCode,resText;

    @SerializedName("data")
    private List<DataBean> data;

    public String getResCode() {
        return resCode;
    }

    public String getResText() {
        return resText;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public void setResText(String resText) {
        this.resText = resText;
    }


    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }


    public static class DataBean {

        private String status;

        private String msg;

        private String id;

        private String userId;

        private String chatMsg;

        private String chatType;

        private String msgType;

        private String time;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getChatMsg() {
            return chatMsg;
        }

        public void setChatMsg(String chatMsg) {
            this.chatMsg = chatMsg;
        }

        public String getChatType() {
            return chatType;
        }

        public void setChatType(String chatType) {
            this.chatType = chatType;
        }

        public String getMsgType() {
            return msgType;
        }

        public void setMsgType(String msgType) {
            this.msgType = msgType;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

}

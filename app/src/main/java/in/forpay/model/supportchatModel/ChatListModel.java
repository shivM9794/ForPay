package in.forpay.model.supportchatModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ChatListModel implements Serializable {

    /**
     * data : [{"id":"813701","user_id":"75064","chat_msg":"hello","chat_type":"1","msg_type":"0","time":"4 mins ","created_date":"2021-01-08 10:25:05","replyBy":""},{"id":"813708","user_id":"75064","chat_msg":"Hlo","chat_type":"0","msg_type":"0","time":"3 mins ","created_date":"2021-01-08 10:25:44","replyBy":"rohit"}]
     * resCode : 200
     * resText : Please update your pan in profile section first
     * supportName : Support
     * supportActiveTime : Last Active 3 minutes
     * supportDetails : {"mobile":"+910000000000","status":"offline","onClickText":"Agent not available right now"}
     */

//    @SerializedName("resCode")
    private String resCode;
//    @SerializedName("resText")
    private String resText;
//    @SerializedName("supportName")
    private String supportName;
//    @SerializedName("supportActiveTime")
    private String supportActiveTime;
    /**
     * mobile : +910000000000
     * status : offline
     * onClickText : Agent not available right now
     */

    @SerializedName("supportDetails")
    private SupportDetailsBean supportDetails;
    /**
     * id : 813701
     * user_id : 75064
     * chat_msg : hello
     * chat_type : 1
     * msg_type : 0
     * time : 4 mins
     * created_date : 2021-01-08 10:25:05
     * replyBy :
     */

    @SerializedName("data")
    private List<DataBean> data;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResText() {
        return resText;
    }

    public void setResText(String resText) {
        this.resText = resText;
    }

    public String getSupportName() {
        return supportName;
    }

    public void setSupportName(String supportName) {
        this.supportName = supportName;
    }

    public String getSupportActiveTime() {
        return supportActiveTime;
    }

    public void setSupportActiveTime(String supportActiveTime) {
        this.supportActiveTime = supportActiveTime;
    }

    public SupportDetailsBean getSupportDetails() {
        return supportDetails;
    }

    public void setSupportDetails(SupportDetailsBean supportDetails) {
        this.supportDetails = supportDetails;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class SupportDetailsBean {
        @SerializedName("mobile")
        private String mobile;
        @SerializedName("status")
        private String status;
        @SerializedName("onClickText")
        private String onClickText;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOnClickText() {
            return onClickText;
        }

        public void setOnClickText(String onClickText) {
            this.onClickText = onClickText;
        }
    }

    public static class DataBean {
        @SerializedName("id")
        private String id;
        @SerializedName("user_id")
        private String userId;
        @SerializedName("chat_msg")
        private String chatMsg;
        @SerializedName("chat_type")
        private String chatType;
        @SerializedName("msg_type")
        private String msgType;
        @SerializedName("time")
        private String time;
        @SerializedName("created_date")
        private String createdDate;
        @SerializedName("replyBy")
        private String replyBy;

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
}

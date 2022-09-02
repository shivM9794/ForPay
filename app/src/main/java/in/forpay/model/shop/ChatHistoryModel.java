package in.forpay.model.shop;

import java.util.List;

public class ChatHistoryModel {


    /**
     * data : [{"id":"27","shopId":"11","msg":"","msgType":"2","chatType":"reply","time":"2020-07-13 11:37:10","toUserId":"25063","name":"Test name"},{"id":"24","shopId":"0","msg":"selleer","msgType":"0","chatType":"response","time":"2020-07-11 09:24:11","toUserId":"25063","name":"Test name"}]
     * resCode : 200
     * resText :
     */

    private String resCode;
    private String resText;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 27
         * shopId : 11
         * msg :
         * msgType : 2
         * chatType : reply
         * time : 2020-07-13 11:37:10
         * toUserId : 25063
         * name : Test name
         */

        private String id;
        private String shopId;
        private String msg;
        private String msgType;
        private String chatType;
        private String time;
        private String toUserId;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getMsgType() {
            return msgType;
        }

        public void setMsgType(String msgType) {
            this.msgType = msgType;
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

        public String getToUserId() {
            return toUserId;
        }

        public void setToUserId(String toUserId) {
            this.toUserId = toUserId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

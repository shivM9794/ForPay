package in.forpay.model.shop;

import java.util.List;

public class ShopChatListModel {


    /**
     * data : [{"id":"15","msg":"Buyer","msgType":"0","chatType":"response","time":"2020-07-06 18:16:36"},{"id":"16","msg":"Buyer","msgType":"0","chatType":"response","time":"2020-07-06 18:17:28"},{"id":"17","msg":"Buyer","msgType":"0","chatType":"response","time":"2020-07-06 18:26:37"},{"id":"18","msg":"Buyer","msgType":"0","chatType":"response","time":"2020-07-07 10:50:23"},{"id":"19","msg":"Buyer test","msgType":"0","chatType":"response","time":"2020-07-07 11:00:22"}]
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
         * id : 15
         * msg : Buyer
         * msgType : 0
         * chatType : response
         * time : 2020-07-06 18:16:36
         */

        private String id;
        private String msg;
        private String msgType;
        private String chatType;
        private String time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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
    }
}

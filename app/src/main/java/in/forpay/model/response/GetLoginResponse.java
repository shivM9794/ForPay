package in.forpay.model.response;

public class GetLoginResponse {


    private String status = "";
    private String resCode = "";
    private String resText = "";
    private Data data;
    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResCode() {
        return resCode;
    }

    public String getResText() {
        return resText;
    }

    public void setResText(String resText) {
        this.resText = resText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private String mobile = "";
        private String token = "";
        private String locationRequired="";

        private String action = "";
        private String showRefer = "";
        private String helpLine="";
        private String firstTimeUser="";
        private String isShop="";

        public String getIsShop() {
            return isShop;
        }

        public String getFirstTimeUser() {
            return firstTimeUser;
        }


        public String getHelpLine() {
            return helpLine;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }




        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getLocationRequired() {
            return locationRequired;
        }

        public void setLocationRequired(String locationRequired) {
            this.locationRequired = locationRequired;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getShowRefer() {
            return showRefer;
        }

        public void setShowRefer(String showRefer) {
            this.showRefer = showRefer;
        }




    }


}

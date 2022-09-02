package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetRechargeHistoryResponse {

    @SerializedName("data")
    private ArrayList<Data> dataList;
    private String resCode = "";
    private String resText,bal,commissionBal,isShop = "";

    public String getIsShop() {
        return isShop;
    }

    public void setBal(String bal) {
        this.bal = bal;
    }

    public void setCommissionBal(String commissionBal) {
        this.commissionBal = commissionBal;
    }

    public String getBal() {
        return bal;
    }

    public String getCommissionBal() {
        return commissionBal;
    }

    @SerializedName("notifications")
    private ArrayList<Notifications> notificationsList = new ArrayList<>();

    public ArrayList<Notifications> getNotificationsList(){
        return notificationsList;
    }
    public void setNotificationsList(ArrayList<Notifications> notificationsList){
        this.notificationsList=notificationsList;
    }

    public ArrayList<Data> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<Data> dataList) {
        this.dataList = dataList;
    }

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

    public class Data {
        private String orderId = "";
        private String status = "";
        private String service = "";
        private String TransId = "";
        private String mobile = "";
        private String amount = "";
        private String creditUsed = "";
        private String cusCreditUsed="";
        private String marginAmount = "";
        private String beneficiaryAccountNumber = "";
        private String beneficiaryName = "";
        private String reqTime = "";
        private String operatorId="";
        private String bottomIconUrl = "";
        private String serviceType="";
        private String jsonData="";
        private String profit,opValue1,opValue2,opValue3,opValue4,opValue5,ccf,paymentMode,operatorIcon,visibleCustomerCopy="";

        public String getServiceType() {
            return serviceType;
        }

        public String getJsonData() {
            return jsonData;
        }

        public String getOperatorIcon() {
            return operatorIcon;
        }

        public String getOpValue1() {
            return opValue1;
        }

        public String getOpValue2() {
            return opValue2;
        }

        public String getOpValue3() {
            return opValue3;
        }

        public String getOpValue4() {
            return opValue4;
        }

        public String getOpValue5() {
            return opValue5;
        }

        public String getCcf() {
            return ccf;
        }

        public String getPaymentMode() {
            return paymentMode;
        }

        public String getProfit() {
            return profit;
        }

        public String getOrderId() {
            return orderId;
        }

        public String getStatus() {
            return status;
        }

        public String getService() {
            return service;
        }

        public String getTransId() {
            return TransId;
        }

        public String getMobile() {
            return mobile;
        }

        public String getAmount() {
            return amount;
        }

        public String getCreditUsed() {
            return creditUsed;
        }

        public String getCusCreditUsed() {
            return cusCreditUsed;
        }


        public String getMarginAmount() {
            return marginAmount;
        }

        public String getBeneficiaryAccountNumber() {
            return beneficiaryAccountNumber;
        }

        public String getBeneficiaryName() {
            return beneficiaryName;
        }

        public String getReqTime() {
            return reqTime;
        }

        public String getOperatorId() {return operatorId;}

        public String getBottomIconUrl() {
            return bottomIconUrl;
        }

        public void setBottomIconUrl(String bottomIconUrl) {
            this.bottomIconUrl = bottomIconUrl;
        }

        public String getVisibleCustomerCopy() {
            return visibleCustomerCopy;
        }

        public void setVisibleCustomerCopy(String visibleCustomerCopy) {
            this.visibleCustomerCopy = visibleCustomerCopy;
        }


    }


    public static class Notifications{
        private String id,details,time,clickable,activity,activityData,type,status="";
        public Notifications(){

        }

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getDetails() {
            return details;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTime() {
            return time;
        }

        public void setClickable(String clickable) {
            this.clickable = clickable;
        }

        public String getClickable() {
            return clickable;
        }

        public void setActivity(String activity) {
            this.activity = activity;
        }

        public String getActivity() {
            return activity;
        }

        public void setActivityData(String activityData) {
            this.activityData = activityData;
        }

        public String getActivityData() {
            return activityData;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}

package in.forpay.model;

import java.util.List;

public class HomeSearchModel {

    /**
     * serviceList : [{"onclick":"profile","name":"My Profile","inputData":""},{"onclick":"pin","name":"Change Pin","inputData":""},{"onclick":"password","name":"Change Password","inputData":""},{"onclick":"electricityBillPayment","name":"Electricity Bill Payment","inputData":""},{"onclick":"postpaidBillPayment","name":"Postpaid Bill Payment","inputData":""},{"onclick":"giftVoucher","name":"Flipkart Gift Voucher","inputData":"flipkart"},{"onclick":"electricityBillPayment","name":"Jharkhand Bill Payment","inputData":"jharkhand"},{"onclick":"postpaidBillPayment","name":"Airtel Postpaid Bill Payment","inputData":"airtel"}]
     * resText : SUCCESS
     */

    private String resText;
    private List<ServiceListBean> serviceList;

    public String getResText() {
        return resText;
    }

    public void setResText(String resText) {
        this.resText = resText;
    }

    public List<ServiceListBean> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<ServiceListBean> serviceList) {
        this.serviceList = serviceList;
    }

    public static class ServiceListBean {
        /**
         * onclick : profile
         * name : My Profile
         * inputData :
         */

        private String onclick;
        private String name;
        private String inputData;
        private String onClickActivity;

        public String getOnClickActivity() {
            return onClickActivity;
        }

        public void setOnClickActivity(String onClickActivity) {
            this.onClickActivity = onClickActivity;
        }

        public String getOnclick() {
            return onclick;
        }

        public void setOnclick(String onclick) {
            this.onclick = onclick;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getInputData() {
            return inputData;
        }

        public void setInputData(String inputData) {
            this.inputData = inputData;
        }
    }
}

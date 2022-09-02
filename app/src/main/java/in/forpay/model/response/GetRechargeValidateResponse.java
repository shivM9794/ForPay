package in.forpay.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class GetRechargeValidateResponse {


    private String resCode = "";
    private String resText = "";


    public String getResCode() {
        return resCode;
    }

    public String getResText() {
        return resText;
    }

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        private String bal = "";
        private String creditUsed = "";
        private String marginAmount = "";
        private String beneficiaryAccountNumber = "";
        private String beneficiaryName = "";
        private String operatorIcon = "";


        private String billAmount = "";
        private String billName, profit, customerPay, serviceCharge, service, type, mobile, starSelected, operatorId = "";

        private PopupData popupData;

        private InsufficientData insufficientData;
        private String validateDetails = "";
        private String uniqId = "";
        private String extraData = "";
        private String outputJson = "";

        public String getOutputJson() {
            return outputJson;
        }

        public String getOperatorId() {
            return operatorId;
        }

        public String getOperatorIcon() {
            return operatorIcon;
        }


        public String getUniqId() {
            return uniqId;
        }

        public String getExtraData() {
            return extraData;
        }

        public String getValidateDetails() {
            return validateDetails;
        }

        public void setValidateDetails(String validateDetails) {
            this.validateDetails = validateDetails;
        }

        public InsufficientData getInsufficientData() {
            return insufficientData;
        }

        public void setInsufficientData(InsufficientData insufficientData) {
            this.insufficientData = insufficientData;
        }

        public PopupData getPopupData() {
            return popupData;
        }

        public void setPopupData(PopupData popupData) {
            this.popupData = popupData;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getBillAmount() {
            return billAmount;
        }

        public void setBillAmount(String billAmount) {
            this.billAmount = billAmount;
        }

        public String getBillName() {
            return billName;
        }

        public void setBillName(String billName) {
            this.billName = billName;
        }

        public String getProfit() {
            return profit;
        }

        public void setProfit(String profit) {
            this.profit = profit;
        }

        public String getCustomerPay() {
            return customerPay;
        }

        public void setCustomerPay(String customerPay) {
            this.customerPay = customerPay;
        }

        public String getServiceCharge() {
            return serviceCharge;
        }

        public void setServiceCharge(String serviceCharge) {
            this.serviceCharge = serviceCharge;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setStarSelected(String starSelected) {
            this.starSelected = starSelected;
        }

        public String getStarSelected() {
            return starSelected;
        }

        private String reqTime = "";


        public String getBal() {
            return bal;
        }


        public String getCreditUsed() {
            return creditUsed;
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


    }

    public static class PopupData implements Parcelable {

        private String title;

        private ArrayList<DataList> dataList;


        protected PopupData(Parcel in) {
            title = in.readString();
            dataList = in.createTypedArrayList(DataList.CREATOR);
        }

        public static final Creator<PopupData> CREATOR = new Creator<PopupData>() {
            @Override
            public PopupData createFromParcel(Parcel in) {
                return new PopupData(in);
            }

            @Override
            public PopupData[] newArray(int size) {
                return new PopupData[size];
            }
        };

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public ArrayList<DataList> getDataList() {
            return dataList;
        }

        public void setDataList(ArrayList<DataList> dataList) {
            this.dataList = dataList;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(title);
            parcel.writeTypedList(dataList);
        }
    }

    public static class DataList implements Parcelable {

        private String label, value;

        protected DataList(Parcel in) {
            label = in.readString();
            value = in.readString();
        }

        public static final Creator<DataList> CREATOR = new Creator<DataList>() {
            @Override
            public DataList createFromParcel(Parcel in) {
                return new DataList(in);
            }

            @Override
            public DataList[] newArray(int size) {
                return new DataList[size];
            }
        };

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(label);
            parcel.writeString(value);
        }
    }

    public static class InsufficientData implements Parcelable {

        private String insufficientBalance, amount, locationRequired;

        protected InsufficientData(Parcel in) {
            insufficientBalance = in.readString();
            amount = in.readString();
            locationRequired = in.readString();
        }

        public static final Creator<InsufficientData> CREATOR = new Creator<InsufficientData>() {
            @Override
            public InsufficientData createFromParcel(Parcel in) {
                return new InsufficientData(in);
            }

            @Override
            public InsufficientData[] newArray(int size) {
                return new InsufficientData[size];
            }
        };

        public String getInsufficientBalance() {
            return insufficientBalance;
        }

        public void setInsufficientBalance(String insufficientBalance) {
            this.insufficientBalance = insufficientBalance;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getLocationRequired() {
            return locationRequired;
        }

        public void setLocationRequired(String locationRequired) {
            this.locationRequired = locationRequired;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(insufficientBalance);
            parcel.writeString(amount);
            parcel.writeString(locationRequired);
        }
    }


}

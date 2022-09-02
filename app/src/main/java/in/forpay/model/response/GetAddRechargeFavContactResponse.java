package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetAddRechargeFavContactResponse {
    @SerializedName("operatorList")
    public ArrayList<OperatorList> operatorList = new ArrayList<>();
    private String resCode;
    private String resText;

    public ArrayList<OperatorList> getOperatorList() {
        return operatorList;
    }

    public void setOperatorList(ArrayList<OperatorList> operatorList) {
        this.operatorList = operatorList;
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

    public class OperatorList{

        private String name;
        private String operatorId;
        private String iconUrl;
        private String bbpsId;
        private String inputType;
        private String minLength;
        private String maxLength;
        private String label;
        private String placeHolder;
        private String serviceType;

        public String getServiceType() {
            return serviceType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOperatorId() {
            return operatorId;
        }

        public void setOperatorId(String operatorId) {
            this.operatorId = operatorId;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getBbpsId() {
            return bbpsId;
        }

        public void setBbpsId(String bbpsId) {
            this.bbpsId = bbpsId;
        }

        public String getInputType() {
            return inputType;
        }

        public void setInputType(String inputType) {
            this.inputType = inputType;
        }

        public String getMinLength() {
            return minLength;
        }

        public void setMinLength(String minLength) {
            this.minLength = minLength;
        }

        public String getMaxLength() {
            return maxLength;
        }

        public void setMaxLength(String maxLength) {
            this.maxLength = maxLength;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getPlaceHolder() {
            return placeHolder;
        }

        public void setPlaceHolder(String placeHolder) {
            this.placeHolder = placeHolder;
        }
    }
}

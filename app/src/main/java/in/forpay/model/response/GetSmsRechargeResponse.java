package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetSmsRechargeResponse {

    @SerializedName("smsOperator")
    private ArrayList<Data> smsOperator = new ArrayList<>();
    private String resCode = "";
    private String resText = "";
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public ArrayList<Data> getSmsOperator() {
        return smsOperator;
    }

    public void setSmsOperator(ArrayList<Data> smsOperator) {
        this.smsOperator = smsOperator;
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

    public class Data{

        private String operator;
        private String code;


        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

    }
}

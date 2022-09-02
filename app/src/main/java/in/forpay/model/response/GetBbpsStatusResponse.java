package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetBbpsStatusResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("issues")
        private ArrayList<Issues> issuesList = new ArrayList<>();
        private String resCode = "";
        private String resText = "";

        public ArrayList<Issues> issuesList() {
            return issuesList;
        }

        public void setIssuesList(ArrayList<Issues> issuesList) {
            this.issuesList = issuesList;
        }

        public String getResCode() {
            return resCode;
        }

        public String getResText() {
            return resText;
        }
    }


    public class Issues {
        private String type = "";
        private String value = "";

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}

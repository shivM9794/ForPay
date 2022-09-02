package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetFPLPlayerlist {

    @SerializedName("data")
    private ArrayList<Datum> data;
    private String resCode;
    private String resText;

    public ArrayList<Datum> getData() {
        return data;
    }

    public String getResCode() {
        return resCode;
    }

    public String getResText() {
        return resText;
    }

    public class Datum {
        public String name;
        public String mobile;
        public String rank;

        public String getName() {
            return name;
        }

        public String getMobile() {
            return mobile;
        }

        public String getRank() {
            return rank;
        }
    }

}

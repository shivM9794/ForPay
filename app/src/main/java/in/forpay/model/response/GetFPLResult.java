package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetFPLResult {

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
        public String getRank() {
            return rank;
        }

        public String getPoint() {
            return point;
        }

        public String rank;
        public String point;
    }

}

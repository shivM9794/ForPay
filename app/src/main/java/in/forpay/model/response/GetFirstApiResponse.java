package in.forpay.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetFirstApiResponse {

    @SerializedName("permissionDetails")
    public ArrayList<Data> permissionDetails = new ArrayList<>();

    public ArrayList<Data> getPermissionDetails() {
        return permissionDetails;
    }

    public void setPermissionDetails(ArrayList<Data> permissionDetails) {
        this.permissionDetails = permissionDetails;
    }

    public class Data{

        private String desc;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}

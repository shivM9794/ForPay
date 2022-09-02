package in.forpay.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TransactionCommissionModel implements Parcelable {

    String type, amount, commission;

    protected TransactionCommissionModel(Parcel in) {
        type = in.readString();
        amount = in.readString();
        commission = in.readString();
    }

    public static final Creator<TransactionCommissionModel> CREATOR = new Creator<TransactionCommissionModel>() {
        @Override
        public TransactionCommissionModel createFromParcel(Parcel in) {
            return new TransactionCommissionModel(in);
        }

        @Override
        public TransactionCommissionModel[] newArray(int size) {
            return new TransactionCommissionModel[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getValueByKey(String key){
        switch (key){
            case "type":
                return type;
            case "amount":
                return amount;
            case "commission":
                return commission;
            default:
                return "";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(type);
        parcel.writeString(amount);
        parcel.writeString(commission);
    }
}

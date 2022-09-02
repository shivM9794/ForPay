package in.forpay.model.balance;

import android.os.Parcel;
import android.os.Parcelable;

public class RechargeTypeModel implements Parcelable {

    String title;
    int image;

    public RechargeTypeModel(String title, int image) {
        this.title = title;
        this.image = image;
    }

    protected RechargeTypeModel(Parcel in) {
        title = in.readString();
        image = in.readInt();
    }

    public static final Creator<RechargeTypeModel> CREATOR = new Creator<RechargeTypeModel>() {
        @Override
        public RechargeTypeModel createFromParcel(Parcel in) {
            return new RechargeTypeModel(in);
        }

        @Override
        public RechargeTypeModel[] newArray(int size) {
            return new RechargeTypeModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeInt(image);
    }
}

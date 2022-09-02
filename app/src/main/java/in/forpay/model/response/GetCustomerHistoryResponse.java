package in.forpay.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class GetCustomerHistoryResponse {

    private Data data;
    private String resCode = "";
    private String resText = "";

    public Data getData() {
        return data;
    }

    public String getResCode() {
        return resCode;
    }

    public String getResText() {
        return resText;
    }

    public class Data {
        private Details details;
        private ArrayList<Beneficiary> beneficiaryList = new ArrayList<>();

        public Details getDetails() {
            return details;
        }

        public ArrayList<Beneficiary> getBeneficiaryList() {
            return beneficiaryList;
        }
    }

    public class Details {
        private String message = "";
        private String name = "";
        private String otpVerified = "";
        private String limit = "";
        private String balance = "";

        public String getMessage() {
            return message;
        }

        public String getName() {
            return name;
        }

        public String getOtpVerified() {
            return otpVerified;
        }

        public String getLimit() {
            return limit;
        }

        public String getBalance() {
            return balance;
        }
    }

    public static class Beneficiary implements Parcelable {
        private String beneficiaryName = "";
        private String beneficiaryMobileNumber = "";
        private String beneficiaryAccountNumber = "";
        private String ifscCode = "";
        private String beneficiaryId = "";

        public String getBeneficiaryName() {
            return beneficiaryName;
        }

        public String getBeneficiaryMobileNumber() {
            return beneficiaryMobileNumber;
        }

        public String getBeneficiaryAccountNumber() {
            return beneficiaryAccountNumber;
        }

        public String getIfscCode() {
            return ifscCode;
        }

        public String getBeneficiaryId() {
            return beneficiaryId;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.beneficiaryName);
            dest.writeString(this.beneficiaryMobileNumber);
            dest.writeString(this.beneficiaryAccountNumber);
            dest.writeString(this.ifscCode);
            dest.writeString(this.beneficiaryId);
        }

        public Beneficiary() {
        }

        protected Beneficiary(Parcel in) {
            this.beneficiaryName = in.readString();
            this.beneficiaryMobileNumber = in.readString();
            this.beneficiaryAccountNumber = in.readString();
            this.ifscCode = in.readString();
            this.beneficiaryId = in.readString();
        }

        public static final Parcelable.Creator<Beneficiary> CREATOR = new Parcelable.Creator<Beneficiary>() {
            @Override
            public Beneficiary createFromParcel(Parcel source) {
                return new Beneficiary(source);
            }

            @Override
            public Beneficiary[] newArray(int size) {
                return new Beneficiary[size];
            }
        };
    }
}

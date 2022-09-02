package in.forpay.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class BbpsStatusModel implements Parcelable {
    private String name, value, placeHolder, minLength, maxLength, line, inputType;
    private List<ValueArray> valueArray;


    protected BbpsStatusModel(Parcel in) {
        name = in.readString();
        value = in.readString();
        placeHolder = in.readString();
        minLength = in.readString();
        maxLength = in.readString();
        line = in.readString();
        inputType = in.readString();
        valueArray = in.createTypedArrayList(ValueArray.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(value);
        dest.writeString(placeHolder);
        dest.writeString(minLength);
        dest.writeString(maxLength);
        dest.writeString(line);
        dest.writeString(inputType);
        dest.writeTypedList(valueArray);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BbpsStatusModel> CREATOR = new Creator<BbpsStatusModel>() {
        @Override
        public BbpsStatusModel createFromParcel(Parcel in) {
            return new BbpsStatusModel(in);
        }

        @Override
        public BbpsStatusModel[] newArray(int size) {
            return new BbpsStatusModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
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

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public List<ValueArray> getValueArray() {
        return valueArray;
    }

    public void setValueArray(List<ValueArray> valueArray) {
        this.valueArray = valueArray;
    }


    public static class ValueArray implements Parcelable {

        String type, value;

        protected ValueArray(Parcel in) {
            type = in.readString();
            value = in.readString();
        }

        public static final Creator<ValueArray> CREATOR = new Creator<ValueArray>() {
            @Override
            public ValueArray createFromParcel(Parcel in) {
                return new ValueArray(in);
            }

            @Override
            public ValueArray[] newArray(int size) {
                return new ValueArray[size];
            }
        };

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(type);
            parcel.writeString(value);
        }
    }





}


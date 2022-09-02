package in.forpay.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class AepsModel implements Parcelable {
    private String name, value, placeHolder, minLength, maxLength, line, inputType;
    private List<ValueArray> valueArray;


    protected AepsModel(Parcel in) {
        name = in.readString();
        value = in.readString();
        placeHolder = in.readString();
        minLength = in.readString();
        maxLength = in.readString();
        line = in.readString();
        inputType = in.readString();
        valueArray = in.createTypedArrayList(ValueArray.CREATOR);
    }

    public static final Creator<AepsModel> CREATOR = new Creator<AepsModel>() {
        @Override
        public AepsModel createFromParcel(Parcel in) {
            return new AepsModel(in);
        }

        @Override
        public AepsModel[] newArray(int size) {
            return new AepsModel[size];
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(value);
        parcel.writeString(placeHolder);
        parcel.writeString(minLength);
        parcel.writeString(maxLength);
        parcel.writeString(line);
        parcel.writeString(inputType);
        parcel.writeTypedList(valueArray);
    }


    public static class ValueArray implements Parcelable {

        String id, name, pidXml;

        protected ValueArray(Parcel in) {
            id = in.readString();
            name = in.readString();
            pidXml = in.readString();
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPidXml() {
            return pidXml;
        }

        public void setPidXml(String pidXml) {
            this.pidXml = pidXml;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(id);
            parcel.writeString(name);
            parcel.writeString(pidXml);
        }
    }





}


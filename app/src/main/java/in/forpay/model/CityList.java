package in.forpay.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CityList {


    private DataBean data;
    private String resCode = "";
    private String resText = "";

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
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

    public static class DataBean {
        private List<BusStopsBean> busStops;

        public List<BusStopsBean> getBusStops() {
            return busStops;
        }

        public void setBusStops(List<BusStopsBean> busStops) {
            this.busStops = busStops;
        }

        public static class BusStopsBean implements Parcelable {
            /**
             * sourceId : 1
             * stopName : Hyderabad
             */

            private String sourceId;
            private String stopName;

            public String getSourceId() {
                return sourceId;
            }

            public void setSourceId(String sourceId) {
                this.sourceId = sourceId;
            }

            public String getStopName() {
                return stopName;
            }

            public void setStopName(String stopName) {
                this.stopName = stopName;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.sourceId);
                dest.writeString(this.stopName);
            }

            public BusStopsBean() {
            }

            protected BusStopsBean(Parcel in) {
                this.sourceId = in.readString();
                this.stopName = in.readString();
            }

            public static final Creator<BusStopsBean> CREATOR = new Creator<BusStopsBean>() {
                @Override
                public BusStopsBean createFromParcel(Parcel source) {
                    return new BusStopsBean(source);
                }

                @Override
                public BusStopsBean[] newArray(int size) {
                    return new BusStopsBean[size];
                }
            };
        }
    }
}

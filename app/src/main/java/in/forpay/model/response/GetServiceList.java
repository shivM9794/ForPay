package in.forpay.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class GetServiceList implements Parcelable {

    private List<GetServiceList.ServiceBean> service;
    private String resCode;
    private String resText;


    protected GetServiceList(Parcel in) {
        service = in.createTypedArrayList(ServiceBean.CREATOR);
        resCode = in.readString();
        resText = in.readString();
    }

    public static final Creator<GetServiceList> CREATOR = new Creator<GetServiceList>() {
        @Override
        public GetServiceList createFromParcel(Parcel in) {
            return new GetServiceList(in);
        }

        @Override
        public GetServiceList[] newArray(int size) {
            return new GetServiceList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(resCode);
        parcel.writeString(resText);
    }
    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public List<ServiceBean> getService() {
        return service;
    }

    public static class ServiceBean implements Parcelable{

        private String id;
        private String serviceName;
        private String serviceType;
        private String imgUrl;
        private String topIconUrl;
        private String isBillFetch;
        private String instruction;
        private String bbpsId;

        private List<GetServiceList.ServiceBean.ParamsBean> params;

        protected ServiceBean(Parcel in) {
            id = in.readString();
            serviceName = in.readString();
            serviceType = in.readString();

            imgUrl = in.readString();
            topIconUrl = in.readString();
            isBillFetch=in.readString();
            instruction=in.readString();
            bbpsId=in.readString();
        }

        public static final Creator<ServiceBean> CREATOR = new Creator<ServiceBean>() {
            @Override
            public ServiceBean createFromParcel(Parcel in) {
                return new ServiceBean(in);
            }

            @Override
            public ServiceBean[] newArray(int size) {
                return new ServiceBean[size];
            }
        };


        public String getBbpsId() {
            return bbpsId;
        }

        public void setBbpsId(String bbpsId) {
            this.bbpsId = bbpsId;
        }

        public String getInstruction() {
            return instruction;
        }

        public void setInstruction(String instruction) {
            this.instruction = instruction;
        }

        public String getIsBillFetch() {
            return isBillFetch;
        }

        public void setIsBillFetch(String isBillFetch) {
            this.isBillFetch = isBillFetch;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getServiceName() {
            return serviceName;
        }

        public String getServiceType() {
            return serviceType;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public void setServiceType(String serviceType) {
            this.serviceType = serviceType;
        }


        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getTopIconUrl() {
            return topIconUrl;
        }

        public void setTopIconUrl(String topIconUrl) {
            this.topIconUrl = topIconUrl;
        }

        public List<GetServiceList.ServiceBean.ParamsBean> getParams() {
            return params;
        }

        public void setParams(List<GetServiceList.ServiceBean.ParamsBean> params) {
            this.params = params;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(id);
            parcel.writeString(serviceName);
            parcel.writeString(serviceType);
            parcel.writeString(imgUrl);
            parcel.writeString(topIconUrl);
            parcel.writeString(isBillFetch);
            parcel.writeString(instruction);
        }

        public static class ParamsBean implements Parcelable{


            private String name;
            private String placeHolder;
            private String label;
            private String inputType;
            private String minLength;
            private String maxLength;

            protected ParamsBean(Parcel in) {
                name = in.readString();
                placeHolder = in.readString();
                label = in.readString();
                inputType = in.readString();
                minLength = in.readString();
                maxLength = in.readString();
            }

            public static final Creator<ParamsBean> CREATOR = new Creator<ParamsBean>() {
                @Override
                public ParamsBean createFromParcel(Parcel in) {
                    return new ParamsBean(in);
                }

                @Override
                public ParamsBean[] newArray(int size) {
                    return new ParamsBean[size];
                }
            };

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPlaceHolder() {
                return placeHolder;
            }

            public void setPlaceHolder(String placeHolder) {
                this.placeHolder = placeHolder;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getInputType() {
                return inputType;
            }

            public void setInputType(String inputType) {
                this.inputType = inputType;
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

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeString(name);
                parcel.writeString(placeHolder);
                parcel.writeString(label);
                parcel.writeString(inputType);
                parcel.writeString(minLength);
                parcel.writeString(maxLength);
            }
        }
    }


}

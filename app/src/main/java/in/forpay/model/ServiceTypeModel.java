package in.forpay.model;

public class ServiceTypeModel {
    private String id = "";
    private String service = "";
    private String type = "";
    private String smsCode = "";

    public ServiceTypeModel() {
    }

    public ServiceTypeModel(String id, String service, String type, String smsCode) {
        this.id = id;
        this.service = service;
        this.type = type;
        this.smsCode = smsCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}

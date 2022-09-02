package in.forpay.model.request;

public class NotificationModel {

    private String id;
    private String detail;
    private String date;
    private String type;
    private String clickable;
    private String activityValue;
    private String activityData;
    private String status;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setActivityValue(String activityValue) {
        this.activityValue = activityValue;
    }

    public String getActivityValue() {
        return activityValue;
    }

    public void setActivityData(String activityData) {
        this.activityData = activityData;
    }

    public void setClickable(String clickable) {
        this.clickable = clickable;
    }


    public String getActivityData() {
        return activityData;
    }

    public String getClickable() {
        return clickable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

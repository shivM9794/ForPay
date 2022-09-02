package in.forpay.model;

public class ContactUsModel {

    private String mobile = "";
    private String name = "";
    private String language = "";
    private String department,status = "";


    public String getStatus() {
        return status;
    }

    public String getLanguage() {
        return language;
    }

    public String getDepartment() {
        return department;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setName(String name) {
        this.name = name;
    }
}

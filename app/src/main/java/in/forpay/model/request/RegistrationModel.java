package in.forpay.model.request;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import in.forpay.BR;

public class RegistrationModel extends BaseObservable {
    private String username = "";
    private String name = "";
    private String mobile = "";
    private String email = "";
    private String password = "";
    private String repeatPassword = "";
    private String pin = "";
    private String areaPinCode = "";
    private String referredBy = "";
    private String type = "";

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
        notifyPropertyChanged(BR.mobile);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
        notifyPropertyChanged(BR.repeatPassword);
    }

    @Bindable
    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
        notifyPropertyChanged(BR.pin);
    }

    @Bindable
    public String getAreaPinCode() {
        return areaPinCode;
    }

    public void setAreaPinCode(String areaPinCode) {
        this.areaPinCode = areaPinCode;
        notifyPropertyChanged(BR.areaPinCode);
    }

    @Bindable
    public String getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(String referredBy) {
        this.referredBy = referredBy;
        notifyPropertyChanged(BR.referredBy);
    }

    @Bindable
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        notifyPropertyChanged(BR.type);
    }
}

package in.forpay.model;

public class FavContactModel {

    private String mobile = "";
    private String name = "";
    private String inputValue1 = "";
    private String inputValue2 = "";
    private String dueAmount="";
    private String onClick="";
    private String operatorId="";
    private String searchData,operatorName,operatorIcon,missCall,canSetMisscall,missCallAmount="";

    public String getMissCallAmount() {
        return missCallAmount;
    }

    public void setMissCallAmount(String missCallAmount) {
        this.missCallAmount = missCallAmount;
    }

    public String getCanSetMisscall() {
        return canSetMisscall;
    }

    public void setCanSetMisscall(String canSetMisscall) {
        this.canSetMisscall = canSetMisscall;
    }

    public String getMissCall() {
        return missCall;
    }

    public void setMissCall(String missCall) {
        this.missCall = missCall;
    }

    public String getOperatorIcon() {
        return operatorIcon;
    }

    public void setOperatorIcon(String operatorIcon) {
        this.operatorIcon = operatorIcon;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public String getSearchData() {
        return searchData;
    }

    public void setSearchData(String searchData) {
        this.searchData = searchData;
    }



    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDueAmount() {
        return dueAmount;
    }

    public String getInputValue1() {
        return inputValue1;
    }

    public String getInputValue2() {
        return inputValue2;
    }

    public String getOnClick() {
        return onClick;
    }

    public void setDueAmount(String dueAmount) {
        this.dueAmount = dueAmount;
    }

    public void setInputValue1(String inputValue1) {
        this.inputValue1 = inputValue1;
    }

    public void setInputValue2(String inputValue2) {
        this.inputValue2 = inputValue2;
    }

    public void setOnClick(String onClick) {
        this.onClick = onClick;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorId() {
        return operatorId;
    }
}

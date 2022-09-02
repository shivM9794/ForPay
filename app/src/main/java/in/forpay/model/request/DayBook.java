package in.forpay.model.request;

public class DayBook {


    private String operatorId,success,pending,disputed,creditUsed,cusCreditUsed,date,profit="";

    public DayBook(){

    }

    public DayBook(String operatorId,String success,String pending,String disputed,String creditUsed,String cusCreditUsed,String date,String profit){
        this.operatorId=operatorId;
        this.success=success;
        this.pending=pending;
        this.disputed=disputed;
        this.creditUsed=creditUsed;
        this.cusCreditUsed=cusCreditUsed;
        this.date=date;
        this.profit=profit;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getCreditUsed() {
        return creditUsed;
    }

    public void setCreditUsed(String creditUsed) {
        this.creditUsed = creditUsed;
    }

    public String getCusCreditUsed() {
        return cusCreditUsed;
    }

    public void setCusCreditUsed(String cusCreditUsed) {
        this.cusCreditUsed = cusCreditUsed;
    }

    public String getDisputed() {
        return disputed;
    }

    public void setDisputed(String disputed) {
        this.disputed = disputed;
    }

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getProfit() {
        return profit;
    }
}

package in.forpay.model.response;

public class GetPaymentSummaryModel {
    String title="";
    String amount="";

    public String getAmount() {
        return amount;
    }

    public String getTitle() {
        return title;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

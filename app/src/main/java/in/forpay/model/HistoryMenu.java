package in.forpay.model;

public class HistoryMenu {

    public String title="";
    public String activity="";

    public String getActivity() {
        return activity;
    }

    public String getTitle() {
        return title;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HistoryMenu(){

    }

    public HistoryMenu(String title,String activity){
        this.activity=activity;
        this.title=title;
    }
}

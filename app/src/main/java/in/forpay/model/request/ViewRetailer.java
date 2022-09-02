package in.forpay.model.request;

public class ViewRetailer {
    private String UserName,name,balance,packageName,status="";

    public ViewRetailer(){

    }

    public ViewRetailer(String UserName , String name , String balance , String packageName , String status){

        this.UserName=UserName;
        this.name=name;
        this.balance=balance;
        this.packageName=packageName;
        this.status=status;

    }

    public String getUserName(){
        return UserName;

    }
    public void setUserName(String UserName){
        this.UserName=UserName;
    }


    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }


    public String getBalance(){
        return balance;
    }

    public void setBalance(String balance){
        this.balance=balance;
    }


    public String getPackageName(){
        return packageName;
    }

    public void setPackageName(String packageName){
        this.packageName=packageName;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status=status;
    }
}

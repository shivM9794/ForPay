package in.forpay.model;

public class ContactList {
    private String name = "";
    private String phoneNumber = "";

    public ContactList(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}

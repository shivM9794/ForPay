package in.forpay.model;

public class Player {
    public Player(String id, String role) {
        this.id = id;
        this.role = role;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String id;
    public String role;
}

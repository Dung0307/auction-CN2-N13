package auction.client.model;

public class Bidder extends User{
    private double balance;

    public Bidder(String id, String name, String password, double balance) {
        super(id, name, password);
        this.balance = balance;
    }
}

package auction.client.model;

public class Bid extends User{
    private double userPrice;
    private User bidder;

    public Bid(User user,double amount) {
        bidder = user;
        userPrice = amount;
    }
}

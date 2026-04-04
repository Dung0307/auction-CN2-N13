package auction.client.model;

import java.util.ArrayList;
import java.util.List;

public class Seller extends User {
    private List<Item> inventory;

    public Seller(String id, String username, String password) {
        super(id, username, password);
        this.inventory = new ArrayList<>();
    }
}

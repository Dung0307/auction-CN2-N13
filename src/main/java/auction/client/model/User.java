package auction.client.model;

import java.util.ArrayList;
import java.util.List;


public class User {
    protected String id;
    protected String username;
    protected String password;
    private List<Product> spdangdaugia;
    private List<Product> sp_seller_uplen;
    public User() {}

    public User(String id,String name,String passwork) {
        this.id = id;
        this.username = name;
        this.password = passwork;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return username;
    }






}


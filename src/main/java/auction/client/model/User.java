package auction.client.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    protected String id;
    protected String username;
    protected String password;
    private List<Product> spdangdaugia;
    private List<Product> sp_seller_uplen;
    private double price;

    public User(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.spdangdaugia = new ArrayList<>();
        this.sp_seller_uplen = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    // Khúc này là chức năng đấu giá sản phẩm của User

    public void addBid(Product bid) {    // Thêm sản phẩm đang đấu giá vào list sau khi User bấm đấu giá
        spdangdaugia.add(bid);
    }

    public void setprice(double price) {
        this.price = price;
    }

    // khúc này là chức năng đăng sản phẩm để đấu giá của User

    public void upProduct(Product sp) {
        sp_seller_uplen.add(sp);
    }

    public List<Product> getAuctions() {
        return sp_seller_uplen;
    }



}


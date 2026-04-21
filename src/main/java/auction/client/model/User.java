package auction.client.model;

import java.util.ArrayList;
import java.util.List;

// 🔴 Abstract class - không thể new User() trực tiếp!
public abstract class User {
    protected String id;
    protected String username;
    protected String password;
    private List<Product> spdangdaugia;
    private List<Product> sp_seller_uplen;

    public User() {}

    public User(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.spdangdaugia = new ArrayList<>();
        this.sp_seller_uplen = new ArrayList<>();
    }

    //abstract method, nhớ viết lại ở subclass

    /**
     * Lấy số dư
     * - Bidder: return balance
     * - Seller: return revenue
     * - Admin: return 0
     */
    public abstract double getBalance();

    /**
     * Lấy role của user
     * - Bidder: "BIDDER"
     * - Seller: "SELLER"
     * - Admin: "ADMIN"
     */
    public abstract String getUserRole();

    // concrete methods, dùng chung cho tất cả subclass

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public List<Product> getSpdangdaugia() {
        return spdangdaugia;
    }

    public List<Product> getSpSellerUplen() {
        return sp_seller_uplen;
    }
}




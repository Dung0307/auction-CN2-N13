package auction.client.model;

import java.util.ArrayList;
import java.util.List;

public class Seller extends User {
    private List<Item> inventory = new ArrayList<>();
    private double revenue = 0;  // Doanh thu từ các phiên đấu giá

    public Seller(String id, String username, String password) {
        super(id, username, password);
    }

    // getters
    @Override
    public double getBalance() {
        // seller thì "balance" = revenue (doanh thu)
        return revenue;
    }

    @Override
    public String getUserRole() {
        return "SELLER";
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public double getRevenue() {
        return revenue;
    }

    // quản lý kho và tính doanh thu

    public void addItem(Item item) {
        inventory.add(item);
    }

    //xóa item đã bán
    public void removeItem(Item item) {
        inventory.remove(item);
    }

    //lấy item từ kho
    public Item findItem(Item targetItem) {
        return inventory.stream()
                .filter(item -> item.equals(targetItem))
                .findFirst()
                .orElse(null);
    }

    //tính doanh thu
    public void addRevenue(double amount) {
        revenue += amount;
    }
}

package auction.client.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
import java.util.UUID;

public class Product {
    // ...existing imports and class definition...

    // Private fields - encapsulation! ✅
    private String id;
    private String name;
    private String price;
    private String imageUrl;
    private String description;
    private LocalDateTime createdAt;

    // Kho chứa chung cho toàn bộ ứng dụng
    public static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    // Constructor
    public Product(String name, String price, String imageUrl, String description) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    // getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // setters - chỉ cho phép thay đổi price (khi có bid mới)
    public void setPrice(String newPrice) {
        this.price = newPrice;
    }
}
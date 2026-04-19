package auction.client.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProductStorage {
    // Kho chứa chung cho toàn bộ ứng dụng
    public String name;
    public String price;
    public String imageUrl;
    public String desc;

    public ProductStorage(String name, String price, String imageUrl, String desc) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.desc = desc;
    }

    public static ObservableList<ProductStorage> allProducts = FXCollections.observableArrayList();
}
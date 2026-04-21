package auction.client.model;

import java.util.UUID; //tạo uuid cho item

public abstract class Item {
    protected String id;
    protected String name;
    protected String description;

    public Item(String name, String description) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
    }

    // abstract methods, nhớ viết lại ở subclass
    //lấy loại item
    public abstract String getItemType();

    //lấy thông số item
    public abstract String getSpecifications();

    //getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

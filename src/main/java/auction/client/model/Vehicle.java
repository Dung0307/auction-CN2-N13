package auction.client.model;

/**
 * Vehicle - Phương tiện giao thông
 * Extends Item → có id, name, description
 */
public class Vehicle extends Item {
    private String brand;
    private String model;
    private int year;           // năm sản xuất
    private long mileage;       // Số km đã chạy

    public Vehicle(String name, String description, String brand,
                   String model, int year, long mileage) {
        super(name, description);
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
    }

    @Override
    public String getItemType() {
        return "VEHICLE";
    }

    @Override
    public String getSpecifications() {
        return String.format(
                "Brand: %s | Model: %s | Year: %d | Mileage: %d km", //%s là string, %d là số nguyên
                brand, model, year, mileage //map theo thứ tự các biến
        );
    }

    //getters
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public long getMileage() { return mileage; }
}


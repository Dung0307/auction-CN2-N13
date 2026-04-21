package auction.client.model;

public class Electronics extends Item {
    private String brand;
    private String model;
    private int warrantyMonths; ////bảo hành (tháng)

    public Electronics(String name, String description, String brand,
                      String model, int warrantyMonths) {
        super(name, description);
        this.brand = brand;
        this.model = model;
        this.warrantyMonths = warrantyMonths;
    }

    @Override
    public String getItemType() {
        return "ELECTRONICS";
    }

    @Override
    public String getSpecifications() {
        return String.format(
                "Brand: %s | Model: %s | Warranty: %d months", //%s là string, %d là số nguyên
                brand, model, warrantyMonths //map theo thứ tự các biến
        );
    }

    //getters
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getWarrantyMonths() { return warrantyMonths; }
}


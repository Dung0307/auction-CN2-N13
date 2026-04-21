package auction.client.model;

//Enum để track trạng thái của một phiên
public enum AuctionStatus {
    OPEN("Chưa bắt đầu"),           // Phiên vừa tạo, chưa ai bid
    RUNNING("Đang diễn ra"),        // Đã có bid, nhưng chưa hết thời gian
    FINISHED("Hết thời gian"),
    PAID("Thanh toán xong"),
    CANCELED("Bị hủy");             // Admin hủy phiên

    private final String description;

    AuctionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}


package auction.client.model;

import java.time.LocalDateTime;
import java.util.UUID;

//Ghi nhận mỗi lần bid, dùng để track lịch sử đấu giá
public class BidTransaction {
    private String bidId;
    private String auctionId;       // id phiên
    private String bidderId;        // ng bid
    private double bidAmount;       // bid bao nhiêu
    private LocalDateTime timestamp; // thời điểm bid

    public BidTransaction(String auctionId, String bidderId, double bidAmount) {
        this.bidId = UUID.randomUUID().toString();
        this.auctionId = auctionId;
        this.bidderId = bidderId;
        this.bidAmount = bidAmount;
        this.timestamp = LocalDateTime.now();
    }

    //getters
    public String getBidId() {
        return bidId;
    }

    public String getAuctionId() {
        return auctionId;
    }

    public String getBidderId() {
        return bidderId;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format(
                "Bid{id=%s, bidder=%s, amount=%.0f, time=%s}",
                bidId, bidderId, bidAmount, timestamp
        );
    }
}


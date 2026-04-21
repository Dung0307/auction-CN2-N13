package auction.client.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//Lifecycle: OPEN → RUNNING → FINISHED → PAID/CANCELED
public class Auction {
    private String auctionId;           // UUID của phiên
    private Product product;
    private Seller seller;
    private AuctionStatus status;       // Trạng thái
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double currentPrice;        // Giá hiện tại
    private Bidder winner;              // Người thắng (null nếu chưa)
    private List<BidTransaction> bidHistory; // Lịch sử bid

    public Auction(Product product, Seller seller, LocalDateTime endTime) {
        this.auctionId = UUID.randomUUID().toString();
        this.product = product;
        this.seller = seller;
        this.status = AuctionStatus.OPEN;
        this.startTime = LocalDateTime.now();
        this.endTime = endTime;
        // Giá bắt đầu = giá của product
        this.currentPrice = Double.parseDouble(
                //lấy giá (string) -> bỏ ký tự không phải số -> parse thành double
                product.getPrice().replaceAll("[^0-9]", "")
        );
        this.bidHistory = new ArrayList<>();
        this.winner = null;
    }

    //getters
    public String getAuctionId() { return auctionId; }
    public Product getProduct() { return product; }
    public Seller getSeller() { return seller; }
    public AuctionStatus getStatus() { return status; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public double getCurrentPrice() { return currentPrice; }
    public Bidder getWinner() { return winner; }
    public List<BidTransaction> getBidHistory() { return bidHistory; }

    //setters
    public void setStatus(AuctionStatus status) { this.status = status; }
    public void setCurrentPrice(double price) { this.currentPrice = price; }
    public void setWinner(Bidder winner) { this.winner = winner; }

    //operations

    //Kiểm tra phiên còn hoạt động không
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(endTime);
    }

    //thêm bid vào log
    public void addBidTransaction(BidTransaction bid) {
        bidHistory.add(bid);
    }

    /**
     * Lấy bid cao nhất
     */
    public BidTransaction getHighestBid() {
        if (bidHistory.isEmpty()) return null;
        return bidHistory.get(bidHistory.size() - 1);
    }

    //Tính thời gian còn lại (phút)
    public long getTimeRemainingMinutes() {
        long seconds = java.time.temporal.ChronoUnit.SECONDS
                .between(LocalDateTime.now(), endTime);
        return seconds / 60;
    }

    @Override
    public String toString() {
        return String.format(
                "Auction{id=%s, product=%s, price=%.0f, status=%s, bids=%d}",
                auctionId, product.getName(), currentPrice, status, bidHistory.size()
        );
    }
}


package auction.client.exception;

public class AuctionClosedException extends Exception {
    public AuctionClosedException() {
        super("Phiên đấu giá đã kết thúc!");
    }
}
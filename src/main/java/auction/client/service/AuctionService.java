package auction.client.service;

import auction.client.exception.AuctionClosedException;
import auction.client.exception.InvalidBidException;
import auction.client.model.Product;

/**
 * AuctionService - Xử lý tất cả logic liên quan đến đấu giá
 * NHIỆM VỤ:
 * - Validate giá đặt
 * - Tính mức giá tiếp theo
 * - Parse & format giá
 * - Xử lý đặt giá
 * - Quản lý trạng thái phiên đấu giá
 * Controller chỉ gọi service này, KHÔNG xử lý logic
 */
public class AuctionService {

    // Tỷ lệ tăng giá mặc định: 5%
    private static final double DEFAULT_BID_INCREMENT_RATIO = 0.05;

    // Trạng thái phiên đấu giá
    private boolean auctionClosed = false;

    /**
     * 1. VALIDATE BID
     * Kiểm tra giá đặt có hợp lệ không
     *
     * @param bidText giá user nhập
     * @param minimumBid giá tối thiểu phải đặt
     */
    public void validateBid(String bidText, double minimumBid)
            throws InvalidBidException, AuctionClosedException {

        // Kiểm tra phiên đấu giá còn hoạt động không
        if (auctionClosed) {
            throw new AuctionClosedException();
        }

        // Kiểm tra input rỗng
        if (bidText == null || bidText.trim().isEmpty()) {
            throw new InvalidBidException("Vui lòng nhập số tiền đấu giá!");
        }

        double bidAmount;

        // Kiểm tra định dạng số
        try {
            bidAmount = Double.parseDouble(bidText);
        } catch (NumberFormatException e) {
            throw new InvalidBidException("Định dạng số tiền không hợp lệ!");
        }

        // Giá phải > 0
        if (bidAmount <= 0) {
            throw new InvalidBidException("Giá đặt phải lớn hơn 0!");
        }

        // Giá phải >= minimum
        if (bidAmount < minimumBid) {
            throw new InvalidBidException(
                    "Giá phải lớn hơn hoặc bằng "
                            + formatPrice(minimumBid) + " VNĐ"
            );
        }
    }

    /**
     * 2. PLACE BID
     * Xử lý đặt giá
     */
    // MBao thêm synchronized vào nhé !!
    public synchronized void placeBid(Product product, double bidAmount)
            throws InvalidBidException, AuctionClosedException {

        // Lấy giá hiện tại từ product
        double currentPrice =
                extractPriceFromString(product.getPrice());

        // Tính mức giá tối thiểu tiếp theo
        double minimumBid =
                calculateNextMinimumBid(currentPrice);

        // Validate bid
        validateBid(String.valueOf(bidAmount), minimumBid);

        // Cập nhật giá mới
        product.setPrice(formatPrice(bidAmount));
    }

    /**
     * 3. TÍNH GIÁ TỐI THIỂU TIẾP THEO
     * currentPrice * 1.05
     */
    public double calculateNextMinimumBid(double currentPrice) {
        return currentPrice * (1 + DEFAULT_BID_INCREMENT_RATIO);
    }

    /**
     * 4. PARSE GIÁ TỪ STRING
     * "1,000,000 VNĐ" -> 1000000
     */
    public double extractPriceFromString(String priceStr) {
        try {
            String cleanPrice = priceStr.replaceAll("[^0-9]", "");
            return Double.parseDouble(cleanPrice);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 5. PARSE BID USER NHẬP
     */
    public double parseBidAmount(String bidText) {
        return Double.parseDouble(bidText);
    }

    /**
     * 6. FORMAT GIÁ
     * 1000000 -> 1,000,000
     */
    public String formatPrice(double price) {
        return String.format("%,.0f", price);
    }

    /**
     * 7. ĐÓNG PHIÊN ĐẤU GIÁ
     */
    public void closeAuction() {
        auctionClosed = true;
    }

    /**
     * 8. KIỂM TRA PHIÊN CÒN HOẠT ĐỘNG
     */
    public boolean isAuctionActive() {
        return !auctionClosed;
    }
}
package auction.client.model;

//Có quyền cao hơn Bidder và Seller

public class Admin extends User {
    public Admin(String id, String username, String password) {
        super(id, username, password);
    }

    @Override
    public double getBalance() {
        // Admin không có số dư
        return 0;
    }

    @Override
    public String getUserRole() {
        return "ADMIN";
    }

    // hành động của Admin

    // khóa tài khoản người dùng
    public void suspendUser(String userId) {
        System.out.println("🚫 Admin " + username + " suspended user: " + userId);
    }

    // đóng phiên đấu giá sớm
    public void closeAuction(String auctionId) {
        System.out.println("⏹️ Admin " + username + " closed auction: " + auctionId);
    }

    // xóa sản phẩm
    public void removeProduct(String productId) {
        System.out.println("🗑️ Admin " + username + " removed product: " + productId);
    }

    // xem log hệ thống
    public void viewSystemReport() {
        System.out.println("📊 Admin " + username + " viewing system report");
    }
}


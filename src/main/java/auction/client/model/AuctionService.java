package auction.client.model;

import static auction.client.model.AuctionItem.notifyUser;
//Đây là mbao viết lúc chuwa biết là đã có AUctionService trước đó rồi
//Để tham khảo
public class AuctionService {
    public synchronized void placeBid(User user, AuctionItem item, double amount) {

        // 1. Validate
        if (amount <= item.getCurrentPrice()) {
            System.out.println("Giá không hợp lệ!");
            return;
        }

        // 2. Tạo Bid mới
        Bid bid = new Bid(user, amount);

        // 3. Cập nhật item
        item.setCurrentPrice(amount);
        item.setHighestBidder(user);

        // 4. Lưu lịch sử
        item.addBid(bid);

        // 5. (Optional) thông báo
        notifyUser(item);
    }

}

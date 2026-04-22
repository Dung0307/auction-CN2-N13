package auction.client.model;

import java.util.ArrayList;
import java.util.List;

public class AuctionItem {

        private double currentPrice;
        private User highestBidder;
        private List<BidTransaction> bidHistory = new ArrayList<>();

        public void addBid(BidTransaction bid) {
            bidHistory.add(bid);
        }

        public double getCurrentPrice() {
            return currentPrice;
        }

        public void setCurrentPrice(double amount) {
            currentPrice = amount;
        }

        public void setHighestBidder(User user) {
            highestBidder = user;
        }

        public static void notifyUser(AuctionItem item) {
            System.out.println("Bạn đã đặt giá thành công");
        }
    }



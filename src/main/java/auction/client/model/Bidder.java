package auction.client.model;

import auction.client.exception.InsufficientBalanceException;
import java.util.ArrayList;
import java.util.List;

public class Bidder extends User {
    private double balance;
    private List<BidTransaction> bidHistory = new ArrayList<>();

    public Bidder(String id, String username, String password, double balance) {
        super(id, username, password);
        this.balance = balance;
    }

    //getters
    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public String getUserRole() {
        return "BIDDER";
    }

    public List<BidTransaction> getBidHistory() {
        return bidHistory;
    }

    // ========== SETTERS & OPERATIONS ==========

    public void setBalance(double amount) {
        this.balance = amount;
    }

//rút tiền khi đặt bid
    public void withdrawBalance(double amount) throws InsufficientBalanceException {
        if (balance < amount) {
            throw new InsufficientBalanceException(
                    "Số dư không đủ! Hiện tại: " + balance + " VNĐ"
            );
        }
        balance -= amount;
    }

    //hoàn tiền khi thua
    public void refundBalance(double amount) {
        balance += amount;
    }

    //lưu lịch sử bid
    public void addBidTransaction(BidTransaction bid) {
        bidHistory.add(bid);
    }
}

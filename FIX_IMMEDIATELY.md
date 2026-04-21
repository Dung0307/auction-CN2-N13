# 🔧 HƯỚNG DẪN SỬA LỖI ĐỒN NGAY

## Priority 1: FIX NGAY HÔM NAY (Easy & Quick wins)

---

## 1️⃣ FIX ENCAPSULATION - Product.java

**Vấn đề:** Public fields vi phạm OOP encapsulation

```java
// ❌ HIỆN TẠI (SAI)
public class Product {
    public String name;
    public String price;
    public String imageUrl;
    public String desc;
}

// ✅ CẦN THÊM (ĐÚNG)
public class Product {
    private String id;
    private String name;
    private String price;
    private String imageUrl;
    private String description;
    private LocalDateTime createdAt;
    
    public Product(String name, String price, String imageUrl, String description) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
    
    // Setter cho price (thay đổi khi có bid mới)
    public void setPrice(String newPrice) { this.price = newPrice; }
}
```

**Tác động:** Phải update ProductService, AddProductController, MainViewController, AuctionDetailController

---

## 2️⃣ FIX ENCAPSULATION - Bidder.java

```java
// ❌ HIỆN TẠI (SAI)
public class Bidder extends User {
    private double balance;
    
    public Bidder(String id, String name, String password, double balance) {
        super(id, name, password);
        this.balance = balance;
    }
}

// ✅ CẦN THÊM
public class Bidder extends User {
    private double balance;
    private List<BidTransaction> bidHistory = new ArrayList<>();
    
    public Bidder(String id, String name, String password, double balance) {
        super(id, name, password);
        this.balance = balance;
    }
    
    // Getters & Setters
    public double getBalance() { return balance; }
    public void setBalance(double amount) { this.balance = amount; }
    public void withdrawBalance(double amount) throws InsufficientBalanceException {
        if (balance < amount) {
            throw new InsufficientBalanceException("Số dư không đủ!");
        }
        balance -= amount;
    }
    public void depositBalance(double amount) {
        balance += amount;
    }
    
    // Bid history
    public List<BidTransaction> getBidHistory() { return bidHistory; }
    public void addBidTransaction(BidTransaction bid) { bidHistory.add(bid); }
    
    @Override
    public double getBalance() { return this.balance; }
}
```

---

## 3️⃣ FIX ENCAPSULATION - Seller.java

```java
// ✅ CẦN THÊM
public class Seller extends User {
    private List<Item> inventory = new ArrayList<>();
    private double revenue = 0;
    
    public Seller(String id, String username, String password) {
        super(id, username, password);
    }
    
    public List<Item> getInventory() { return inventory; }
    public void addItem(Item item) { inventory.add(item); }
    public void removeItem(String itemId) { 
        inventory.removeIf(item -> item.getId().equals(itemId)); 
    }
    
    public double getRevenue() { return revenue; }
    public void addRevenue(double amount) { revenue += amount; }
    
    @Override
    public double getBalance() { return revenue; }  // polymorphism
}
```

---

## 4️⃣ FIX OOP - Thêm abstract methods vào User.java

```java
// ✅ CẦN SỬA
public abstract class User {  // <-- Phải thêm abstract
    protected String id;
    protected String username;
    protected String password;
    private List<Product> spdangdaugia;
    private List<Product> sp_seller_uplen;
    
    public User(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.spdangdaugia = new ArrayList<>();
        this.sp_seller_uplen = new ArrayList<>();
    }
    
    // Abstract methods (bắt Bidder, Seller, Admin phải implement)
    public abstract double getBalance();
    public abstract String getUserRole();
    
    // Concrete methods
    public String getId() { return id; }
    public String getUsername() { return username; }
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
```

---

## 5️⃣ THÊM Admin.java

```java
package auction.client.model;

public class Admin extends User {
    public Admin(String id, String username, String password) {
        super(id, username, password);
    }
    
    @Override
    public double getBalance() {
        return 0;  // Admin không có balance
    }
    
    @Override
    public String getUserRole() {
        return "ADMIN";
    }
    
    // Admin actions
    public void suspendUser(String userId) {
        System.out.println("Admin suspended user: " + userId);
    }
    
    public void closeAuction(String auctionId) {
        System.out.println("Admin closed auction: " + auctionId);
    }
}
```

---

## 6️⃣ THÊM Item Subclasses

**Hiện tại Item.java:**
```java
public abstract class Item {
}
```

**Cần sửa:**

```java
// Item.java
package auction.client.model;

import java.util.UUID;

public abstract class Item {
    protected String id;
    protected String name;
    protected String description;
    
    public Item(String name, String description) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
    }
    
    public abstract String getItemType();
    public abstract String getSpecifications();
    
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
}

// Electronics.java
public class Electronics extends Item {
    private String brand;
    private String model;
    private int warrantyMonths;
    
    public Electronics(String name, String description, String brand, String model, int warranty) {
        super(name, description);
        this.brand = brand;
        this.model = model;
        this.warrantyMonths = warranty;
    }
    
    @Override
    public String getItemType() { return "ELECTRONICS"; }
    
    @Override
    public String getSpecifications() {
        return "Brand: " + brand + ", Model: " + model + ", Warranty: " + warrantyMonths + " months";
    }
}

// Art.java
public class Art extends Item {
    private String artist;
    private String medium;
    private int year;
    
    public Art(String name, String description, String artist, String medium, int year) {
        super(name, description);
        this.artist = artist;
        this.medium = medium;
        this.year = year;
    }
    
    @Override
    public String getItemType() { return "ART"; }
    
    @Override
    public String getSpecifications() {
        return "Artist: " + artist + ", Medium: " + medium + ", Year: " + year;
    }
}

// Vehicle.java
public class Vehicle extends Item {
    private String brand;
    private String model;
    private int year;
    private long mileage;
    
    public Vehicle(String name, String description, String brand, String model, int year, long mileage) {
        super(name, description);
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
    }
    
    @Override
    public String getItemType() { return "VEHICLE"; }
    
    @Override
    public String getSpecifications() {
        return brand + " " + model + " (" + year + "), Mileage: " + mileage + "km";
    }
}
```

---

## 7️⃣ THÊM BidTransaction.java

```java
package auction.client.model;

import java.time.LocalDateTime;

public class BidTransaction {
    private String bidId;
    private String auctionId;
    private String bidderId;
    private double bidAmount;
    private LocalDateTime timestamp;
    
    public BidTransaction(String auctionId, String bidderId, double bidAmount) {
        this.bidId = java.util.UUID.randomUUID().toString();
        this.auctionId = auctionId;
        this.bidderId = bidderId;
        this.bidAmount = bidAmount;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters
    public String getBidId() { return bidId; }
    public String getAuctionId() { return auctionId; }
    public String getBidderId() { return bidderId; }
    public double getBidAmount() { return bidAmount; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
```

---

## 8️⃣ THÊM Auction.java

```java
package auction.client.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Auction {
    private String auctionId;
    private Product product;
    private Seller seller;
    private AuctionStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double currentPrice;
    private Bidder winner;
    private List<BidTransaction> bidHistory;
    
    public Auction(Product product, Seller seller, LocalDateTime endTime) {
        this.auctionId = java.util.UUID.randomUUID().toString();
        this.product = product;
        this.seller = seller;
        this.status = AuctionStatus.OPEN;
        this.startTime = LocalDateTime.now();
        this.endTime = endTime;
        this.currentPrice = Double.parseDouble(product.getPrice().replaceAll("[^0-9]", ""));
        this.bidHistory = new ArrayList<>();
    }
    
    // Getters & Setters
    public String getAuctionId() { return auctionId; }
    public Product getProduct() { return product; }
    public Seller getSeller() { return seller; }
    public AuctionStatus getStatus() { return status; }
    public void setStatus(AuctionStatus status) { this.status = status; }
    
    public LocalDateTime getEndTime() { return endTime; }
    public boolean isExpired() { return LocalDateTime.now().isAfter(endTime); }
    
    public double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(double price) { this.currentPrice = price; }
    
    public Bidder getWinner() { return winner; }
    public void setWinner(Bidder winner) { this.winner = winner; }
    
    public List<BidTransaction> getBidHistory() { return bidHistory; }
    public void addBidTransaction(BidTransaction bid) { bidHistory.add(bid); }
}
```

---

## 9️⃣ THÊM AuctionStatus Enum

```java
package auction.client.model;

public enum AuctionStatus {
    OPEN,           // Chưa bắt đầu
    RUNNING,        // Đang diễn ra
    FINISHED,       // Hết thời gian
    PAID,           // Thanh toán thành công
    CANCELED        // Bị hủy
}
```

---

## 🔟 THÊM InsufficientBalanceException.java

```java
package auction.client.exception;

public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
```

---

## 1️⃣1️⃣ UPDATE AuctionService - Thêm Concurrency

```java
package auction.client.service;

import auction.client.exception.AuctionClosedException;
import auction.client.exception.InvalidBidException;
import auction.client.model.Product;
import java.util.concurrent.locks.ReentrantLock;

public class AuctionService {
    private static final double DEFAULT_BID_INCREMENT_RATIO = 0.05;
    private boolean auctionClosed = false;
    
    // 🔒 Lock cho concurrent bidding
    private final ReentrantLock bidLock = new ReentrantLock();
    
    /**
     * Thread-safe placeBid với ReentrantLock
     */
    public void placeBid(Product product, double bidAmount)
            throws InvalidBidException, AuctionClosedException {
        
        bidLock.lock();  // 🔒 ACQUIRE LOCK
        try {
            // Critical section - chỉ 1 thread có thể execute cùng lúc
            double currentPrice = extractPriceFromString(product.price);
            double minimumBid = calculateNextMinimumBid(currentPrice);
            
            validateBid(String.valueOf(bidAmount), minimumBid);
            product.price = formatPrice(bidAmount);
            
            System.out.println("✅ Bid placed successfully: " + formatPrice(bidAmount));
        } finally {
            bidLock.unlock();  // 🔓 RELEASE LOCK
        }
    }
    
    // ... rest of methods remain same
}
```

---

## 1️⃣2️⃣ THÊM Singleton Pattern - AuctionManager.java

```java
package auction.client.service;

import auction.client.model.Auction;
import java.util.ArrayList;
import java.util.List;

/**
 * AuctionManager - Quản lý tất cả phiên đấu giá
 * Singleton Pattern: chỉ có 1 instance duy nhất
 */
public class AuctionManager {
    private static AuctionManager instance;  // Static reference
    private List<Auction> auctions;
    
    // Private constructor - không thể new từ ngoài
    private AuctionManager() {
        this.auctions = new ArrayList<>();
    }
    
    // Lấy instance (tạo nếu chưa có)
    public static synchronized AuctionManager getInstance() {
        if (instance == null) {
            instance = new AuctionManager();
        }
        return instance;
    }
    
    public void addAuction(Auction auction) {
        auctions.add(auction);
    }
    
    public List<Auction> getAllAuctions() {
        return new ArrayList<>(auctions);
    }
    
    public Auction getAuctionById(String auctionId) {
        return auctions.stream()
                .filter(a -> a.getAuctionId().equals(auctionId))
                .findFirst()
                .orElse(null);
    }
}
```

**Cách dùng:**
```java
AuctionManager manager = AuctionManager.getInstance();
manager.addAuction(auction);
```

---

## 1️⃣3️⃣ THÊM Factory Pattern - ItemFactory.java

```java
package auction.client.service;

import auction.client.model.*;

/**
 * ItemFactory - Tạo các loại Item khác nhau
 * Factory Pattern: tập trung logic tạo object
 */
public class ItemFactory {
    
    public static Item createItem(ItemType type, String name, String description, 
                                   Object... args) {
        switch(type) {
            case ELECTRONICS:
                return new Electronics(name, description, 
                    (String) args[0],      // brand
                    (String) args[1],      // model
                    (Integer) args[2]);    // warranty
                    
            case ART:
                return new Art(name, description,
                    (String) args[0],      // artist
                    (String) args[1],      // medium
                    (Integer) args[2]);    // year
                    
            case VEHICLE:
                return new Vehicle(name, description,
                    (String) args[0],      // brand
                    (String) args[1],      // model
                    (Integer) args[2],     // year
                    (Long) args[3]);       // mileage
                    
            default:
                throw new IllegalArgumentException("Unknown item type: " + type);
        }
    }
}

public enum ItemType {
    ELECTRONICS, ART, VEHICLE
}
```

**Cách dùng:**
```java
Item electronics = ItemFactory.createItem(ItemType.ELECTRONICS, 
    "iPhone 15", "Latest phone",
    "Apple", "iPhone 15 Pro", 24);
```

---

## 1️⃣4️⃣ THÊM Observer Pattern - BidObserver.java

```java
package auction.client.observer;

import auction.client.model.BidTransaction;

public interface BidObserver {
    void onNewBid(BidTransaction bid);
}
```

**Update AuctionService:**
```java
public class AuctionService {
    private List<BidObserver> observers = new ArrayList<>();
    private List<BidTransaction> bidHistory = new ArrayList<>();
    
    // Subscribe observer
    public void addObserver(BidObserver observer) {
        observers.add(observer);
    }
    
    // Unsubscribe observer
    public void removeObserver(BidObserver observer) {
        observers.remove(observer);
    }
    
    // Notify all observers
    private void notifyObservers(BidTransaction bid) {
        for (BidObserver observer : observers) {
            observer.onNewBid(bid);
        }
    }
    
    // Update placeBid
    public void placeBid(Product product, double bidAmount) {
        // ... existing logic
        
        // Create transaction
        BidTransaction bid = new BidTransaction(
            product.getName(),
            "current_bidder_id",
            bidAmount
        );
        
        bidHistory.add(bid);
        notifyObservers(bid);  // 📢 NOTIFY OBSERVERS
    }
}
```

**Implement Observer (trong Controller):**
```java
public class AuctionDetailController implements BidObserver, Initializable {
    
    @Override
    public void onNewBid(BidTransaction bid) {
        // Update UI khi có bid mới
        System.out.println("🔔 New bid: " + bid.getBidAmount());
    }
}
```

---

## 1️⃣5️⃣ THÊM Auto-Close Logic - AuctionScheduler.java

```java
package auction.client.service;

import auction.client.model.Auction;
import auction.client.model.AuctionStatus;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class AuctionScheduler {
    private Timer timer;
    private static final long CHECK_INTERVAL = 1000; // 1 giây
    
    public AuctionScheduler() {
        timer = new Timer();
    }
    
    /**
     * Bắt đầu kiểm tra và auto-close phiên hết hạn
     */
    public void startAutoCloseTask() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkAndCloseExpiredAuctions();
            }
        }, 0, CHECK_INTERVAL);
    }
    
    /**
     * Kiểm tra phiên đấu giá hết hạn và đóng
     */
    private void checkAndCloseExpiredAuctions() {
        AuctionManager manager = AuctionManager.getInstance();
        
        for (Auction auction : manager.getAllAuctions()) {
            // Nếu phiên chưa kết thúc nhưng hết thời gian
            if (auction.getStatus() == AuctionStatus.RUNNING 
                && auction.isExpired()) {
                
                closeAuction(auction);
            }
        }
    }
    
    /**
     * Đóng phiên đấu giá
     */
    private void closeAuction(Auction auction) {
        auction.setStatus(AuctionStatus.FINISHED);
        
        // Xác định người thắng (người đặt giá cao nhất)
        if (!auction.getBidHistory().isEmpty()) {
            var lastBid = auction.getBidHistory()
                .get(auction.getBidHistory().size() - 1);
            System.out.println("🏆 Auction finished! Winner: " + 
                lastBid.getBidderId());
        }
        
        System.out.println("⏹️ Auction closed: " + auction.getAuctionId());
    }
    
    public void stop() {
        timer.cancel();
    }
}
```

---

## NEXT STEPS

1. ✅ Tạo các file mới ở trên
2. ✅ Update Product.java (private fields + getter/setter)
3. ✅ Update Bidder.java & Seller.java (add getter/setter)
4. ✅ Update User.java (thêm abstract, thêm abstract methods)
5. ✅ Update AuctionService (thêm ReentrantLock)
6. ✅ Update ProductService (reference product.getId() thay vì name)
7. ✅ Update AuctionDetailController (sử dụng product.getPrice())
8. ✅ Update AddProductController (sử dụng product.getId())

---

**Lưu ý:** Sau khi sửa, các file Controller cần update reference vì Product không còn public fields!



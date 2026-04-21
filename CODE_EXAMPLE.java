/**
 * HƯỚNG DẪN CODE STEP-BY-STEP
 * Cách gửi request đặt giá lên server
 */

// ===================================================================
// PHẦN 1: SETUP - BẢN CHẠY SERVER TRƯỚC
// ===================================================================

/**
 * FILE: AuctionServer.java
 * PACKAGE: auction.client.server
 *
 * Khởi động server lắng nghe client
 */
public class AuctionServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(9999)) {
            System.out.println("🚀 Auction Server started on port 9999");

            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("📨 New client connected");

                // Mỗi client là một thread riêng
                handleClient clientHandler = new handleClient();
                new Thread(() -> clientHandler.handleClient(client)).start();
            }
        } catch (Exception e) {
            System.err.println("❌ Server error: " + e.getMessage());
        }
    }
}


// ===================================================================
// PHẦN 2: CLIENT GỬI REQUEST
// ===================================================================

/**
 * FILE: AuctionClient.java
 * PACKAGE: auction.client.network
 *
 * Gửi request "BID|auctionId|userId|bidAmount" đến server
 */
public class AuctionClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 9999;

    /**
     * Gửi request đặt giá
     * Format: "BID|auctionId|userId|bidAmount"
     *
     * Ví dụ:
     *   auctionId = 1
     *   userId = 123
     *   bidAmount = 510000000
     *
     *   Request = "BID|1|123|510000000"
     *   Response = "SUCCESS" or "FAIL|error message"
     */
    public static boolean sendBidToServer(int auctionId, int userId, double bidAmount) {
        try {
            // 1️⃣ KẾT NỐI ĐẾN SERVER
            System.out.println("🔌 Connecting to server...");
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            System.out.println("✅ Connected!");

            // 2️⃣ TẠO STREAMS
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 3️⃣ FORMAT REQUEST
            String request = "BID|" + auctionId + "|" + userId + "|" + bidAmount;
            System.out.println("📤 Sending: " + request);

            // 4️⃣ GỬI REQUEST
            out.println(request);  // ← Request được gửi qua socket

            // 5️⃣ ĐỢI RESPONSE
            String response = in.readLine();
            System.out.println("📥 Received: " + response);

            // 6️⃣ ĐÓNG KẾT NỐI
            socket.close();
            System.out.println("🔌 Connection closed");

            // 7️⃣ KIỂM TRA KẾT QUẢ
            return response != null && response.startsWith("SUCCESS");

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            return false;
        }
    }
}

// Cách sử dụng trong Controller:
// boolean result = AuctionClient.sendBidToServer(1, 123, 510000000);
// if (result) {
//     showSuccessPopup("✅ Success");
// } else {
//     showErrorPopup("❌ Failed");
// }


// ===================================================================
// PHẦN 3: SERVER NHẬN VÀ XỬ LÝ REQUEST
// ===================================================================

/**
 * FILE: handleClient.java
 * PACKAGE: auction.client.server
 *
 * Xử lý request từ client
 */
public class handleClient {

    private AuctionService auctionService = new AuctionService();
    private ProductService productService = new ProductService();

    public void handleClient(Socket socket) {
        try {
            // 1️⃣ TẠO STREAMS TỪ CLIENT SOCKET
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
            PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true
            );

            // 2️⃣ NHẬN REQUEST
            String request = in.readLine();
            System.out.println("📥 Server received: " + request);

            // 3️⃣ PARSE REQUEST
            String[] parts = request.split("\\|");

            // 4️⃣ KIỂM TRA COMMAND
            if (parts[0].equals("BID")) {
                try {
                    // EXTRACT DATA
                    int auctionId = Integer.parseInt(parts[1]);
                    int userId = Integer.parseInt(parts[2]);
                    double price = Double.parseDouble(parts[3]);

                    System.out.println("  ├─ auctionId: " + auctionId);
                    System.out.println("  ├─ userId: " + userId);
                    System.out.println("  ├─ price: " + price);

                    // TÌM PRODUCT (TODO: Implement mapping)
                    Product product = productService.findProductByName("Product_" + auctionId);

                    if (product != null) {
                        // VALIDATE & UPDATE (service xử lý)
                        auctionService.placeBid(product, price);

                        // GỬI RESPONSE THÀNH CÔNG
                        out.println("SUCCESS");
                        System.out.println("✅ Response: SUCCESS");

                    } else {
                        // PRODUCT KHÔNG TÌM THẤY
                        out.println("FAIL|Product not found");
                        System.out.println("❌ Response: FAIL|Product not found");
                    }

                } catch (InvalidBidException e) {
                    // LỖI VALIDATE GIÁ
                    out.println("FAIL|" + e.getMessage());
                    System.out.println("❌ Response: FAIL|" + e.getMessage());

                } catch (AuctionClosedException e) {
                    // PHIÊN ĐÃ ĐÓNG
                    out.println("FAIL|Auction closed");
                    System.out.println("❌ Response: FAIL|Auction closed");
                }
            }

            // 5️⃣ ĐÓNG KẾT NỐI
            socket.close();
            System.out.println("🔌 Connection closed");

        } catch (Exception e) {
            System.err.println("❌ Error in handleClient: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


// ===================================================================
// PHẦN 4: CLIENT CONTROLLER GỬI REQUEST
// ===================================================================

/**
 * FILE: AuctionDetailController.java
 * PACKAGE: auction.client.controller
 *
 * Controller hiển thị chi tiết sản phẩm + button đặt giá
 */
public class AuctionDetailController implements Initializable {

    @FXML
    private TextField bidAmountField;

    private final AuctionService auctionService = new AuctionService();
    private double currentMinimumBid = 0;
    private Product currentProduct = null;

    // TODO: Lấy từ login session
    private int currentUserId = 1;
    private int currentAuctionId = 1;

    /**
     * Được gọi khi user bấm nút "🚀 ĐỒNG BỘ GIÁ"
     */
    @FXML
    public void placeBid() {

        // 1️⃣ KIỂM TRA PRODUCT ĐÃ TẢI CHƯA
        if (currentProduct == null) {
            showErrorPopup("Error", "Product not loaded!");
            return;
        }

        // 2️⃣ LẤY INPUT TỪ UI
        String bidText = bidAmountField.getText();
        System.out.println("👤 User entered: " + bidText);

        try {
            // 3️⃣ VALIDATE GIÁ (CLIENT-SIDE)
            auctionService.validateBid(bidText, currentMinimumBid);
            System.out.println("✅ Validation passed");

            // 4️⃣ PARSE GIÁ
            double bidAmount = auctionService.parseBidAmount(bidText);
            System.out.println("💰 Bid amount: " + bidAmount);

            // 5️⃣ UPDATE LOCAL (CLIENT-SIDE)
            auctionService.placeBid(currentProduct, bidAmount);
            System.out.println("✅ Local price updated: " + currentProduct.price);

            // 6️⃣ ⭐ GỬI REQUEST LÊN SERVER
            System.out.println("\n🌐 Sending to server...");
            boolean serverResponse = AuctionClient.sendBidToServer(
                currentAuctionId,
                currentUserId,
                bidAmount
            );

            // 7️⃣ HANDLE RESPONSE
            if (serverResponse) {
                showSuccessPopup(
                    "Success ✅",
                    "Your bid has been registered on the server!"
                );
                System.out.println("✅ Server confirmed the bid");
            } else {
                showErrorPopup(
                    "Warning ⚠️",
                    "Bid is valid but failed to sync with server.\n" +
                    "Please check your connection."
                );
                System.out.println("⚠️ Server connection failed");
            }

            // 8️⃣ CẬP NHẬT UI
            currentPriceLabel.setText(currentProduct.price);
            currentMinimumBid = auctionService.calculateNextMinimumBid(
                auctionService.extractPriceFromString(currentProduct.price)
            );
            nextBidLabel.setText("* Min next bid: " +
                auctionService.formatPrice(currentMinimumBid) + " VNĐ");

            // 9️⃣ XÓA INPUT
            bidAmountField.clear();
            System.out.println("✅ UI updated and cleared\n");

        } catch (InvalidBidException e) {
            System.out.println("❌ Invalid bid: " + e.getMessage());
            showErrorPopup("Invalid Bid ❌", e.getMessage());

        } catch (AuctionClosedException e) {
            System.out.println("❌ Auction closed: " + e.getMessage());
            showErrorPopup("Auction Closed ❌", e.getMessage());

        } catch (Exception e) {
            System.out.println("❌ System error: " + e.getMessage());
            showErrorPopup("Error ❌", "System error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showSuccessPopup(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorPopup(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


// ===================================================================
// PHẦN 5: CONSOLE OUTPUT EXAMPLE
// ===================================================================

/**
 * KHI CHẠY:
 *
 * --- CONSOLE SERVER ---
 * 🚀 Auction Server started on port 9999
 * 📨 New client connected: /127.0.0.1
 * 📥 Server received: BID|1|123|510000000
 *   ├─ auctionId: 1
 *   ├─ userId: 123
 *   ├─ price: 510000000.0
 * ✅ Response: SUCCESS
 * 🔌 Connection closed
 *
 * --- CONSOLE CLIENT ---
 * 👤 User entered: 510000000
 * ✅ Validation passed
 * 💰 Bid amount: 510000000.0
 * ✅ Local price updated: 510,000,000
 *
 * 🌐 Sending to server...
 * 🔌 Connecting to server...
 * ✅ Connected!
 * 📤 Sending: BID|1|123|510000000
 * 📥 Received: SUCCESS
 * 🔌 Connection closed
 * ✅ Server confirmed the bid
 * ✅ UI updated and cleared
 */


// ===================================================================
// PHẦN 6: DIAGRAM KẾT NỐI
// ===================================================================

/**
 *
 * ┌──────────────────┐                    ┌──────────────────┐
 * │   CLIENT APP     │                    │   SERVER         │
 * │   (JavaFX)       │                    │   (Socket)       │
 * └─────────┬────────┘                    └────────┬─────────┘
 *           │                                       │
 *           │ (1) User clicks button               │
 *           ├──────────────────────────────────────┤
 *           │                                       │
 *           │ (2) placeBid() called                │
 *           ├──────────────────────────────────────┤
 *           │                                       │
 *           │ (3) validateBid() - local check       │
 *           ├──────────────────────────────────────┤
 *           │                                       │
 *           │ (4) parseBidAmount()                  │
 *           ├──────────────────────────────────────┤
 *           │                                       │
 *           │ (5) placeBid() - update local price   │
 *           ├──────────────────────────────────────┤
 *           │                                       │
 *           │ (6) Socket created                   │
 *           ├──────────────────────────────────────┤
 *           │                                       │
 *           │ (7) "BID|1|123|510000000" ──────────>│
 *           │                                       │
 *           │                              handleClient()
 *           │                                    │
 *           │                              (A) parse request
 *           │                              (B) findProduct()
 *           │                              (C) validateBid()
 *           │                              (D) placeBid()
 *           │                                    │
 *           │ <────────── "SUCCESS" ────────────┤
 *           │                                       │
 *           │ (8) Receive response                 │
 *           ├──────────────────────────────────────┤
 *           │                                       │
 *           │ (9) Close socket                     │
 *           ├──────────────────────────────────────┤
 *           │                                       │
 *           │ (10) Update UI                       │
 *           ├──────────────────────────────────────┤
 *           │                                       │
 *      ✅ DONE                                  (wait)
 *           │                                       │
 */


// ===================================================================
// PHẦN 7: TEST CASES
// ===================================================================

/**
 * TEST 1: Valid bid
 * Input:  "510000000"
 * Min:    "505000000"
 * Result: ✅ SUCCESS
 *
 *
 * TEST 2: Bid too low
 * Input:  "500000000"
 * Min:    "505000000"
 * Result: ❌ FAIL - "Giá phải lớn hơn 505,000,000"
 *
 *
 * TEST 3: Server not running
 * Input:  "510000000"
 * Result: ❌ Connection refused (Port 9999 not open)
 * UI:     "Failed to sync with server"
 *
 *
 * TEST 4: Invalid format
 * Input:  "abc"
 * Result: ❌ NumberFormatException
 * UI:     "Invalid bid format"
 *
 *
 * TEST 5: Empty input
 * Input:  ""
 * Result: ❌ InvalidBidException
 * UI:     "Please enter bid amount"
 */



package auction.client.controller;

import auction.client.exception.AuctionClosedException;
import auction.client.exception.InvalidBidException;
import auction.client.model.Product;
import auction.client.network.AuctionClient;
import auction.client.service.AuctionService;
import auction.client.service.ProductService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import static auction.client.network.AuctionClient.sendBidToServer;

public class AuctionDetailController implements Initializable {

    // 1. KHAI BÁO CÁC BIẾN GIAO DIỆN (@FXML)
    @FXML
    private Label breadcrumbLabel;
    @FXML
    private ImageView productImage;
    @FXML
    private Label productTitle;
    @FXML
    private Label conditionLabel;
    @FXML
    private Label productIdLabel;
    @FXML
    private Label currentPriceLabel;
    @FXML
    private Label nextBidLabel;
    @FXML
    private Label storeNameLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private TextField bidAmountField;
    @FXML
    private Label categoryLabel;

    // ✅ Service instance
    private final AuctionService auctionService = new AuctionService();
    private double currentMinimumBid = 0;
    private Product currentProduct = null;  // ✅ LƯU PRODUCT HIỆN TẠI

    private final ProductService productService = new ProductService();
    private static final String DEFAULT_PLACEHOLDER = "https://via.placeholder.com/600x600/0B0B10/D8A95C?text=NO+IMAGE+DATA";

    // ✅ Thông tin người dùng (tạm thời - cần lấy từ session login)
    private int currentUserId = 1;  // TODO: Lấy từ User login
    private int currentAuctionId = 1;  // TODO: Lấy từ Product ID

    // 2. HÀM KHỞI TẠO (Bộ lọc thông minh không làm mất focus)
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();
            if (!text.matches("[0-9]*")) {
                change.setText(text.replaceAll("[^0-9]", ""));
            }
            return change;
        };
        bidAmountField.setTextFormatter(new TextFormatter<>(filter));

    }

    // 3. HÀM ĐỔ DỮ LIỆU TỪ TRANG MAIN
    public void setProductData(String name, String price, String imageUrl, String category,
                               String condition, String productId,
                               String nextBid, String storeName, String description) {

        breadcrumbLabel.setText(name);
        productTitle.setText(name);
        currentPriceLabel.setText(price);

        // cập nhật tên sản phẩm
        categoryLabel.setText(category);

        conditionLabel.setText("Tình trạng: " + condition);
        productIdLabel.setText("Mã SP: " + productId);
        nextBidLabel.setText("* Giá đặt tiếp theo tối thiểu: " + nextBid);
        storeNameLabel.setText(storeName);
        descriptionLabel.setText(description);

        System.out.println("🖼️ Đang tải ảnh chi tiết từ: " + imageUrl); // In ra để dễ debug
        Image image = productService.loadImageWithFallback(imageUrl, DEFAULT_PLACEHOLDER);
        productImage.setImage(image);

        // ✅ TẠO PRODUCT OBJECT VÀ LƯU LẠI
        currentProduct = new Product(name, price, imageUrl, description, category);

        // ✅ Sử dụng service để extract giá
        try {
            currentMinimumBid = auctionService.extractPriceFromString(nextBid);
        } catch (Exception e) {
            currentMinimumBid = 0;
        }
    }

    // 4. HÀM ĐẶT GIÁ (Sử dụng AuctionService để validate)

    /**
     * 4. HÀM ĐẶT GIÁ
     * Controller chỉ:
     * - lấy input từ UI
     * - gọi service
     * - hiển thị popup
     */
    @FXML
    public void placeBid() {

        // Kiểm tra product có được tải chưa
        if (currentProduct == null) {
            showErrorPopup("Lỗi", "Sản phẩm chưa được tải!");
            return;
        }

        // Lấy giá user nhập
        String bidText = bidAmountField.getText();

        try {

            // 1. Validate bid bằng service
            auctionService.validateBid(bidText, currentMinimumBid);

            // 2. Parse thành số
            double bidAmount =
                    auctionService.parseBidAmount(bidText);

            // 3. ✅ Gọi service với currentProduct (KHÔNG PHẢI NULL)
            auctionService.placeBid(currentProduct, bidAmount);

            // 4. 🌐 GỬI REQUEST LÊN SERVER
            boolean serverResponse = AuctionClient.sendBidToServer(
                    currentAuctionId,
                    currentUserId,
                    bidAmount
            );

            // 5. Hiển thị kết quả
            if (serverResponse) {
                showSuccessPopup(
                        "Đấu giá hợp lệ",
                        "Chúc mừng! Giá đấu của bạn đã được ghi nhận trên server."
                );
            } else {
                showErrorPopup(
                        "Cảnh báo",
                        "Giá đặt hợp lệ nhưng không gửi được lên server.\n" +
                                "Vui lòng kiểm tra kết nối!"
                );
            }

            // 6. Cập nhật giá trên UI
            currentPriceLabel.setText(currentProduct.price);
            currentMinimumBid = auctionService.calculateNextMinimumBid(
                    auctionService.extractPriceFromString(currentProduct.price)
            );
            nextBidLabel.setText("* Giá đặt tiếp theo tối thiểu: " +
                    auctionService.formatPrice(currentMinimumBid) + " VNĐ");

            // 7. Clear input
            bidAmountField.clear();

        } catch (InvalidBidException e) {

            // Lỗi giá đặt không hợp lệ
            showErrorPopup("Giá đặt không hợp lệ", e.getMessage());

        } catch (AuctionClosedException e) {

            // Phiên đấu giá đã đóng
            showErrorPopup("Phiên đã đóng", e.getMessage());

        } catch (Exception e) {

            // Lỗi hệ thống
            showErrorPopup(
                    "Lỗi hệ thống",
                    "Có lỗi xảy ra khi xử lý đấu giá: " + e.getMessage()
            );
        }
    }

    private void showErrorPopup(String title, String message) {
        showCustomPopup(Alert.AlertType.ERROR, title, message, "#e74c3c", "❌");
    }

    private void showSuccessPopup(String title, String message) {
        showCustomPopup(Alert.AlertType.INFORMATION, title, message, "#27AE60", "✅");
    }

    private void showCustomPopup(Alert.AlertType type, String title, String message, String hexColor, String emojiIcon) {
        Alert alert = new Alert(type);
        alert.setTitle("TÍN HIỆU HỆ THỐNG");
        alert.setHeaderText(null);
        alert.setGraphic(null);


        VBox content = new VBox(15);
        content.setStyle(
                "-fx-alignment: center;" +
                        "-fx-padding: 30px 40px;" +
                        "-fx-background-color: #0B0B10;" +
                        "-fx-background-radius: 4px;"
        );

        Label iconLabel = new Label(emojiIcon);
        iconLabel.setStyle("-fx-font-size: 60px;");

        Label titleLabel = new Label(title.toUpperCase());
        titleLabel.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI', system-ui, sans-serif;" +
                        "-fx-text-fill: " + hexColor + ";" +
                        "-fx-letter-spacing: 2px;"
        );

        Label messageLabel = new Label(message);
        messageLabel.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-text-fill: #A0A0B0;" +
                        "-fx-text-alignment: center;" +
                        "-fx-font-family: 'Segoe UI', system-ui, sans-serif;"
        );
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(350);

        // Gom tất cả vào khối
        content.getChildren().addAll(iconLabel, titleLabel, messageLabel);

        // --- TRANG TRÍ DIALOGPANE ---
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setContent(content);
        dialogPane.setStyle(
                "-fx-background-color: #0B0B10;" +
                        "-fx-border-color: #4A4A5A;" +
                        "-fx-border-width: 1px 1px 1px 4px;" +
                        "-fx-border-radius: 4px;" +
                        "-fx-background-radius: 4px;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 15, 0.5, 0, 0);"
        );


        Button okButton = (Button) dialogPane.lookupButton(alert.getButtonTypes().getFirst());
        okButton.setText("XÁC NHẬN");
        okButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #242430, #1A1A24);" +
                        "-fx-text-fill: #D8A95C;" +
                        "-fx-border-color: #4A4A5A;" +
                        "-fx-border-radius: 2px;" +
                        "-fx-background-radius: 2px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 8px 20px;"
        );

        alert.showAndWait();
    }
}
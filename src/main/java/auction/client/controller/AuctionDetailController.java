package auction.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class AuctionDetailController implements Initializable {

    // ========================================================
    // 1. KHAI BÁO CÁC BIẾN GIAO DIỆN (@FXML)
    // ========================================================
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
    private LineChart<String, Number> priceHistoryChart;

    private double currentMinimumBid = 0;

    // ========================================================
    // 2. HÀM KHỞI TẠO (Bộ lọc thông minh không làm mất focus)
    // ========================================================
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();
            if (!text.matches("[0-9]*")) {
                // Tự động xóa chữ cái, chỉ giữ lại số
                change.setText(text.replaceAll("[^0-9]", ""));
            }
            return change; // Trả về change để giữ nguyên con trỏ chuột
        };
        bidAmountField.setTextFormatter(new TextFormatter<>(filter));
    }

    // ========================================================
    // 3. HÀM ĐỔ DỮ LIỆU TỪ TRANG MAIN
    // ========================================================
    public void setProductData(String name, String price, String imageUrl,
                               String condition, String productId,
                               String nextBid, String storeName, String description) {

        breadcrumbLabel.setText(name);
        productTitle.setText(name);
        currentPriceLabel.setText(price);

        conditionLabel.setText("Tình trạng: " + condition);
        productIdLabel.setText("Mã SP: " + productId);
        nextBidLabel.setText("* Giá đặt tiếp theo tối thiểu: " + nextBid);
        storeNameLabel.setText(storeName);
        descriptionLabel.setText(description);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            productImage.setImage(new Image(imageUrl));
        }

        try {
            String cleanNumber = nextBid.replaceAll("[^0-9]", "");
            currentMinimumBid = Double.parseDouble(cleanNumber);
        } catch (Exception e) {
            currentMinimumBid = 0;
        }
    }

    // ========================================================
    // 4. HÀM ĐẶT GIÁ (Xử lý logic và gọi Popup)
    // ========================================================
    @FXML
    public void placeBid(ActionEvent event) {
        String bidText = bidAmountField.getText();

        if (bidText == null || bidText.trim().isEmpty()) {
            showErrorPopup("Lỗi nhập liệu", "Vui lòng nhập số tiền bạn muốn đấu giá!");
            return;
        }

        try {
            double bidAmount = Double.parseDouble(bidText);

            if (bidAmount < currentMinimumBid) {
                showErrorPopup("Giá đặt không hợp lệ",
                        "Mức giá của bạn quá thấp.\nVui lòng đặt lớn hơn hoặc bằng " + String.format("%,.0f", currentMinimumBid) + " VNĐ.");
                return;
            }

            // Thành công
            System.out.println("Backend log: Đặt giá " + bidAmount + " thành công!");
            showSuccessPopup("Đấu giá hợp lệ", "Chúc mừng! Giá đấu của bạn đã được ghi nhận và đang là mức giá cao nhất hiện tại.");
            bidAmountField.clear();

        } catch (NumberFormatException e) {
            showErrorPopup("Lỗi hệ thống", "Định dạng số tiền không đúng!");
        }
    }

    // ========================================================
    // 5. CẬP NHẬT: POPUP THEME HONKAI: STAR RAIL (HSR)
    // ========================================================

    // Hàm gọi Popup Báo Lỗi HSR (Màu Đỏ/Maroon)
    private void showErrorPopup(String title, String message) {
        showCustomPopup(Alert.AlertType.ERROR, title, message, "#e74c3c", "❌");
    }

    // Hàm gọi Popup Thành Công HSR (Màu Vàng Kim/Amber)
    private void showSuccessPopup(String title, String message) {
        showCustomPopup(Alert.AlertType.INFORMATION, title, message, "#D8A95C", "🎉");
    }

    // Lõi tạo Popup HSR: Thiết kế lại hoàn toàn DialogPane
    private void showCustomPopup(Alert.AlertType type, String title, String message, String hexColor, String emojiIcon) {
        Alert alert = new Alert(type);
        alert.setTitle("TÍN HIỆU HỆ THỐNG"); // Thay đổi tiêu đề cho phù hợp chủ đề sci-fi
        alert.setHeaderText(null);
        alert.setGraphic(null); // Bỏ icon mặc định của JavaFX

        // --- CẤU TRÚC LẠI NỘI DUNG POPUP ---
        VBox content = new VBox(15); // Tăng spacing
        content.setStyle(
                "-fx-alignment: center;" +
                        "-fx-padding: 30px 40px;" +
                        "-fx-background-color: #0B0B10;" + // Màu nền tối sâu (deep dark)
                        "-fx-background-radius: 4px;" // Bo góc nhẹ
        );

        // Icon Emoji bự hơn
        Label iconLabel = new Label(emojiIcon);
        iconLabel.setStyle("-fx-font-size: 60px;"); // Icon rất bự ở giữa

        // Tiêu đề HSR: Viết hoa, đậm, màu điểm nhấn
        Label titleLabel = new Label(title.toUpperCase()); // Viết hoa toàn bộ
        titleLabel.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI', system-ui, sans-serif;" + // Font chữ hiện đại
                        "-fx-text-fill: " + hexColor + ";" +
                        "-fx-letter-spacing: 2px;" // Thêm spacing giữa các chữ
        );

        // Nội dung thông báo HSR: Màu xám sáng (A0A0B0), căn giữa
        Label messageLabel = new Label(message);
        messageLabel.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-text-fill: #A0A0B0;" + // Màu chữ xám sáng (A0A0B0)
                        "-fx-text-alignment: center;" +
                        "-fx-font-family: 'Segoe UI', system-ui, sans-serif;"
        );
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(350); // Tăng chiều rộng tối đa

        // Gom tất cả vào khối
        content.getChildren().addAll(iconLabel, titleLabel, messageLabel);

        // --- TRANG TRÍ DIALOGPANE ---
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setContent(content);
        dialogPane.setStyle(
                "-fx-background-color: #0B0B10;" +
                        "-fx-border-color: #4A4A5A;" + // Viền kim loại tối
                        "-fx-border-width: 1px 1px 1px 4px;" + // Viền trái dày hơn làm điểm nhấn
                        "-fx-border-radius: 4px;" +
                        "-fx-background-radius: 4px;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 15, 0.5, 0, 0);" // Đổ bóng sâu
        );

        // Tùy chỉnh Nút bấm HSR
        Button okButton = (Button) dialogPane.lookupButton(alert.getButtonTypes().get(0));
        okButton.setText("XÁC NHẬN"); // Thay đổi chữ nút bấm
        okButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #242430, #1A1A24);" + // Gradient tối
                        "-fx-text-fill: #D8A95C;" + // Chữ màu vàng kim (Amber)
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
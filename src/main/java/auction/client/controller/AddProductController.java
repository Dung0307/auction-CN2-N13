package auction.client.controller;

import auction.client.model.Product;
import auction.client.service.ProductService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox; // ĐÃ THÊM IMPORT NÀY
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.UnaryOperator;

public class AddProductController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField stepField;
    @FXML
    private TextField imageUrlField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private ImageView imagePreview;

    // ĐÃ THÊM: Khai báo ComboBox để chọn loại sản phẩm
    @FXML
    private ComboBox<String> categoryBox;

    // ✅ Service instance
    private ProductService productService = new ProductService();
    private static final String DEFAULT_PLACEHOLDER = "https://via.placeholder.com/350x350/0B0B10/D8A95C?text=NO+IMAGE+DATA";

    @FXML
    public void initialize() {
        // ĐÃ THÊM: Nạp danh sách các loại sản phẩm cho menu xổ xuống
        categoryBox.getItems().addAll(
                "Đồng hồ cao cấp",
                "Dụng cụ thể thao",
                "Thiết bị điện tử",
                "Nghệ thuật & Sưu tầm",
                "Thời trang & Trang sức",
                "Sản phẩm đấu giá Khác"
        );

        // Cài đặt một ảnh mặc định (Placeholder) khi chưa có link
        imagePreview.setImage(new Image(DEFAULT_PLACEHOLDER));

        // Listener xem trước ảnh khi user nhập URL
        imageUrlField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.trim().isEmpty()) {
                Image previewImage = productService.loadImageWithFallback(newValue, DEFAULT_PLACEHOLDER);
                imagePreview.setImage(previewImage);
            } else {
                imagePreview.setImage(new Image(DEFAULT_PLACEHOLDER));
            }
        });
    }

    @FXML
    public void handleCancel() {
        System.out.println("Đã hủy quá trình đồng bộ...");
    }

    @FXML
    public void handleSaveProduct(ActionEvent event) {
        // 1. Lấy dữ liệu từ Form
        String name = nameField.getText();
        String price = priceField.getText().replaceAll("[^0-9]", "");
        String imageUrl = imageUrlField.getText();
        String desc = descriptionArea.getText();

        // Lấy dữ liệu từ ComboBox
        String category = categoryBox.getValue();

        try {
            // 2. ✅ GỌI SERVICE ĐỂ THÊM PRODUCT
            // ĐÃ SỬA: Truyền thêm biến category vào hàm addProduct
            productService.addProduct(name, price, imageUrl, desc, category);

            // 3. QUAY TRỞ VỀ MÀN HÌNH CHÍNH (MainView.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/fxml/MainView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Auction Hub - Trang chủ");

        } catch (IllegalArgumentException e) {
            // ✅ Xử lý lỗi từ service
            System.out.println("❌ Lỗi: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("❌ Lỗi chuyển trang chủ: " + e.getMessage());
        }
    }
}
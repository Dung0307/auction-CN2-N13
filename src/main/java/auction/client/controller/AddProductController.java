package auction.client.controller;

import auction.client.model.Product;
import auction.client.service.ProductService;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

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

    // ✅ Service instance
    private ProductService productService = new ProductService();
    private static final String DEFAULT_PLACEHOLDER = "https://via.placeholder.com/350x350/0B0B10/D8A95C?text=NO+IMAGE+DATA";

    @FXML
    public void initialize() {
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

        try {
            // 2. ✅ GỌI SERVICE ĐỂ THÊM PRODUCT
            // Service sẽ tự động validate dữ liệu
            productService.addProduct(name, price, imageUrl, desc);

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

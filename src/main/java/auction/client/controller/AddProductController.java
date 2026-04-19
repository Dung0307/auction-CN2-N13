package auction.client.controller;

import auction.client.model.Product;
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

    @FXML
    public void initialize() {
        // Cài đặt một ảnh mặc định (Placeholder) khi chưa có link
        // Có thể dùng một icon cảnh báo hoặc logo của game
        String defaultPlaceholder = "https://via.placeholder.com/350x350/0B0B10/D8A95C?text=NO+IMAGE+DATA";
        imagePreview.setImage(new Image(defaultPlaceholder));

        imageUrlField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.trim().isEmpty()) {
                try {
                    // Cố gắng tải ảnh từ URL mới nhập (thêm true để tải ngầm không làm đơ app)
                    Image newImage = new Image(newValue, true);

                    newImage.errorProperty().addListener((obs, oldError, newError) -> {
                        if (newError) {
                            imagePreview.setImage(new Image(defaultPlaceholder));
                        }
                    });

                    imagePreview.setImage(newImage);
                } catch (Exception e) {
                    imagePreview.setImage(new Image(defaultPlaceholder));
                }
            } else {
                imagePreview.setImage(new Image(defaultPlaceholder));
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

        if (name.isEmpty() || price.isEmpty() || imageUrl.isEmpty()) {
            System.out.println("Lỗi: Thiếu thông tin!");
            return;
        }

        // 2. TẠO ĐỐI TƯỢNG VÀ LƯU VÀO KHO CHUNG
        Product newProduct = new Product(name, price, imageUrl, desc);
        Product.allProducts.add(newProduct);

        try {
            // 3. QUAY TRỞ VỀ MÀN HÌNH CHÍNH (MainView.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/fxml/MainView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root); // Dùng setRoot để chuyển cảnh mượt mà
            stage.setTitle("Auction Hub - Trang chủ");

        } catch (IOException e) {
            System.out.println("Lỗi chuyển trang chủ: " + e.getMessage());
        }
    }

}

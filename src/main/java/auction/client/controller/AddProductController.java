package auction.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AddProductController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField stepField; // Mới thêm
    @FXML
    private TextField imageUrlField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private ImageView imagePreview; // Mới thêm

    @FXML
    public void initialize() {
        // Cài đặt một ảnh mặc định (Placeholder) khi chưa có link
        // Có thể dùng một icon cảnh báo hoặc logo của game
        String defaultPlaceholder = "https://via.placeholder.com/350x350/0B0B10/D8A95C?text=NO+IMAGE+DATA";
        imagePreview.setImage(new Image(defaultPlaceholder));

        // LẮNG NGHE SỰ KIỆN: Cứ mỗi khi nội dung ô Link Ảnh thay đổi
        imageUrlField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.trim().isEmpty()) {
                try {
                    // Cố gắng tải ảnh từ URL mới nhập (thêm true để tải ngầm không làm đơ app)
                    Image newImage = new Image(newValue, true);

                    // Kiểm tra xem ảnh có bị lỗi (link hỏng) không
                    newImage.errorProperty().addListener((obs, oldError, newError) -> {
                        if (newError) {
                            imagePreview.setImage(new Image(defaultPlaceholder));
                        }
                    });

                    imagePreview.setImage(newImage);
                } catch (Exception e) {
                    // Nếu dán bậy bạ không phải link thì gán lại ảnh mặc định
                    imagePreview.setImage(new Image(defaultPlaceholder));
                }
            } else {
                imagePreview.setImage(new Image(defaultPlaceholder));
            }
        });

        // (Tùy chọn) Dũng có thể copy lại đoạn code Format tiền tệ
        // cho priceField và stepField vào đây giống hệt như DetailController
    }

    @FXML
    public void handleCancel() {
        System.out.println("Đã hủy quá trình đồng bộ...");
    }

    @FXML
    public void handleSaveProduct() {
        System.out.println("Tên: " + nameField.getText());
        System.out.println("Giá: " + priceField.getText());
        System.out.println("Bước nhảy: " + stepField.getText());
        System.out.println("Ảnh: " + imageUrlField.getText());
    }
}

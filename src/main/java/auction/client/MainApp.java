package auction.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    // nơi vòng đời của ứng dụng giao tiếp với người dùng
    public void start(Stage stage) { // cửa sổ chính của app
        try {
            // Load giao diện từ file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/fxml/LoginView.fxml"));
            // nút gốc chứa toàn bộ các thành phần giao diện khác
            Parent root = loader.load();

            Scene scene = new Scene(root, 900, 600);

            stage.setTitle("UET Auction System");
            // gắn cảnh vào cửa sổ
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.err.println("Lỗi khởi động: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
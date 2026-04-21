package auction.client.controller;

import auction.client.network.AuctionClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class LoginViewController {

    @FXML
    TextField loginText;
    @FXML
    PasswordField passworkText;


    @FXML
    public void login(ActionEvent event) {
        String userName = loginText.getText();
        String passwork = passworkText.getText();

        // Kiểm tra input rỗng
        if (userName == null || userName.trim().isEmpty()) {
            showErrorPopup("Lỗi", "Vui lòng nhập tên tài khoản!");
            return;
        }

        if (passwork == null || passwork.trim().isEmpty()) {
            showErrorPopup("Lỗi", "Vui lòng nhập mật khẩu!");
            return;
        }

        boolean loginResult = AuctionClient.sendLoginRequestToServer(userName, passwork);

        if (loginResult) {
            Platform.runLater(() -> {
                try {
                    SceneSwitcher.switchScene(
                            (Node) event.getSource(),
                            "/client/fxml/MainView.fxml"
                    );
                } catch (IOException e) {
                    showErrorPopup("Lỗi", "Không thể chuyển trang: " + e.getMessage());
                }
            });
        } else {
            showErrorPopup("Đăng nhập thất bại", "Tên tài khoản hoặc mật khẩu không chính xác");
        }
    }

    /**
     * Hiển thị popup lỗi
     */
    private void showErrorPopup(String title, String message) {
        Platform.runLater(() -> showCustomPopup(Alert.AlertType.ERROR, title, message, "#e74c3c", "❌"));
    }

    /**
     * Hiển thị popup tùy chỉnh
     */
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

        content.getChildren().addAll(iconLabel, titleLabel, messageLabel);

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

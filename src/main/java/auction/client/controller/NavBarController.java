package auction.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class NavBarController {
    @FXML
    public void handleSwitchToHome(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/fxml/MainView.fxml"));
            Parent root = loader.load();
            Node currentNode = (Node) event.getSource();
            Stage stage = (Stage) currentNode.getScene().getWindow();
            Scene scene = stage.getScene();
            scene.setRoot(root);
            stage.setTitle("HomePage");
            System.out.println("thay doi");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // NGUYÊN LÝ CHUYỂN TRANG: Tải khuôn AddProduct.fxml và dán đè lên màn hình hiện tại
    @FXML
    public void goToAddProduct(ActionEvent event) {
        try {
            // Tải file giao diện Thêm sản phẩm
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/fxml/AddProduct.fxml"));
            Parent root = loader.load();

            // Lấy cửa sổ hiện tại và thay thế nội dung (Root)
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Auction Hub - Đăng Sản Phẩm Mới");
            stage.getScene().setRoot(root);

        } catch (java.io.IOException e) {
            System.out.println("Lỗi: Không tìm thấy file AddProduct.fxml!");
            e.printStackTrace();
        }
    }

}

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
            SceneSwitcher.switchScene(
                    (Node) event.getSource(),
                    "/client/fxml/MainView.fxml"
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToAddProduct(ActionEvent event) {
        try {
            //dùng controller switcher đã tạo để chuyển cảnh
            SceneSwitcher.switchScene(
                    (Node) event.getSource(),
                    "/client/fxml/AddProduct.fxml"
            );

        } catch (java.io.IOException e) {
            System.out.println("Lỗi: Không tìm thấy file AddProduct.fxml!");
            e.printStackTrace();
        }
    }

}

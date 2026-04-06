package auction.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    TextField loginText;

    @FXML
    public void login(ActionEvent event) throws IOException {
        String userName = loginText.getText();

        SceneSwitcher.switchScene(
                (Node) event.getSource(),
                "/fxml/MainView.fxml"
        );
    }
}

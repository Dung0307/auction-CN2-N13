package auction.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginViewController {

    @FXML
    TextField loginText;

    @FXML
    public void login(ActionEvent event) throws IOException {
        String userName = loginText.getText();

        SceneSwitcher.switchScene(
                (Node) event.getSource(),
                "/client/fxml/MainView.fxml"
        );
    }
}

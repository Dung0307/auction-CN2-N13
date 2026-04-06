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
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    public void login(ActionEvent event) throws IOException {
        String userName = loginText.getText();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
        root = loader.load();


        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}

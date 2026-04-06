package auction.client.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneSwitcher {

    public static void switchScene(Node node, String fxml) throws IOException {
        Parent root = FXMLLoader.load(SceneSwitcher.class.getResource(fxml));
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}

package auction.client.controller;

import auction.client.model.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private FlowPane productContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (productContainer != null) {
            productContainer.getChildren().clear();
        }

        for (Product product : Product.allProducts) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/fxml/ProductCard.fxml"));
                Parent card = loader.load();

                ProductCardController cardController = loader.getController();
                cardController.setData(product);

                productContainer.getChildren().add(card);
            } catch (Exception e) {
                System.out.println("❌ Lỗi load thẻ sản phẩm!");
                e.printStackTrace();
            }
        }
    }
}

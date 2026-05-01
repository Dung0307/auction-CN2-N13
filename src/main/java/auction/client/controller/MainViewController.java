package auction.client.controller;

import auction.client.model.Product;
import auction.client.service.ProductService;
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

    // Service instance
    private ProductService productService = new ProductService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (productContainer != null) {
            productContainer.getChildren().clear();
        }

        // ✅ Sử dụng service để lấy tất cả sản phẩm
        for (Product product : productService.getAllProducts()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/fxml/ProductCard.fxml"));
                Parent card = loader.load();

                ProductCardController cardController = loader.getController();
                cardController.setData(product);

                productContainer.getChildren().add(card);
            } catch (Exception e) {
                System.out.println("❌ Lỗi load thẻ sản phẩm: " + product.name);
                e.printStackTrace();
            }
        }
    }
}

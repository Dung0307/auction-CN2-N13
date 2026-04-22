package auction.client.controller;

import auction.client.model.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ProductCardController {

    @FXML
    private ImageView cardImage;
    @FXML
    private Label cardName;
    @FXML
    private Label cardPrice;

    private Product currentProduct;

    public void setData(Product product) {
        this.currentProduct = product;
        cardName.setText(product.getName());
        cardPrice.setText(product.getPrice() + " VNĐ");

        try {
            cardImage.setImage(new Image(product.getImageUrl(), true));
        } catch (Exception e) {

        }
    }

    @FXML
    public void handleBid(ActionEvent event) {
        try {
            System.out.println("Khách hàng đang xem: " + currentProduct.getName());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/fxml/AuctionDetail.fxml"));
            Parent root = loader.load();

            AuctionDetailController detailController = loader.getController();
            detailController.setProductData(
                    currentProduct.getName(),
                    currentProduct.getPrice() + " VNĐ",
                    currentProduct.getImageUrl(),
                    "Tình trạng: Mới",
                    "#SP-" + (int) (Math.random() * 1000),
                    (Double.parseDouble(currentProduct.getPrice()) + 1000000) + " VNĐ",
                    "Người bán ẩn danh",
                    currentProduct.getDescription()
            );

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Auction Hub - Chi tiết: " + currentProduct.getName());
            stage.getScene().setRoot(root);

        } catch (Exception e) {
            System.out.println("Lỗi chuyển sang trang chi tiết!");
            e.printStackTrace();
        }
    }
}
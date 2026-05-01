package auction.client.controller;

import auction.client.model.Product;
import auction.client.service.ProductService; // THÊM IMPORT NÀY
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

    // TẠO INSTANCE CỦA PRODUCT SERVICE
    private ProductService productService = new ProductService();
    private static final String DEFAULT_PLACEHOLDER = "https://via.placeholder.com/350x350/0B0B10/D8A95C?text=NO+IMAGE+DATA";

    public void setData(Product product) {
        this.currentProduct = product;
        cardName.setText(product.name);
        cardPrice.setText(product.price + " VNĐ");

        // DÙNG SERVICE ĐỂ TẢI ẢNH (CÓ FALLBACK NẾU LỖI)
        Image image = productService.loadImageWithFallback(product.imageUrl, DEFAULT_PLACEHOLDER);
        cardImage.setImage(image);
    }

    @FXML
    public void handleBid(ActionEvent event) {
        try {
            System.out.println("Khách hàng đang xem: " + currentProduct.name);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/fxml/AuctionDetail.fxml"));
            Parent root = loader.load();

            AuctionDetailController detailController = loader.getController();

            // CHÚ Ý ĐOẠN NÀY: Phải truyền đủ 9 tham số
            detailController.setProductData(
                    currentProduct.name,
                    currentProduct.price + " VNĐ",
                    currentProduct.imageUrl,
                    currentProduct.category, // <--- ĐẢM BẢO CÓ DÒNG NÀY (Tham số thứ 4)
                    "Tình trạng: Mới",
                    "#SP-" + (int) (Math.random() * 1000),
                    (Double.parseDouble(currentProduct.price.replaceAll("[^0-9]", "")) + 1000000) + " VNĐ",
                    "Người bán ẩn danh",
                    currentProduct.desc
            );

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Auction Hub - Chi tiết: " + currentProduct.name);
            stage.getScene().setRoot(root);

        } catch (Exception e) {
            System.out.println("Lỗi chuyển sang trang chi tiết!");
            e.printStackTrace();
        }
    }
}
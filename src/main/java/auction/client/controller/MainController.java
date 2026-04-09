package auction.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    // Hàm này sẽ tự động chạy khi bạn bấm nút "Đấu giá ngay" ở bất kỳ sản phẩm nào
    @FXML
    public void handleBid(ActionEvent event) {
        try {
            // 1. Lấy thông tin nút vừa bị bấm xem là sản phẩm nào (Nhờ thuộc tính userData trong FXML)
            Button clickedButton = (Button) event.getSource();
            String itemName = (String) clickedButton.getUserData();
            System.out.println("=====================================");
            System.out.println("Khách hàng đang xem: " + itemName);
            System.out.println("=====================================");

            // 2. Tải file giao diện Chi tiết (AuctionDetail.fxml)
            // LƯU Ý: Hãy đảm bảo đường dẫn này khớp với cây thư mục của bạn (như hồi làm MainView)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/fxml/AuctionDetail.fxml"));
            Parent newRoot = loader.load();

            // 3. Lấy cái cửa sổ (Stage) hiện tại đang hiển thị
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 4. Đổi tiêu đề cửa sổ theo tên sản phẩm đang xem cho chuyên nghiệp
            stage.setTitle("Auction Hub - Chi tiết: " + itemName);

            // 5. THAY ROOT: Bóc trang danh sách hiện tại ra, dán trang chi tiết vào
            // Cách này giúp màn hình chuyển mượt mà, không bị chớp hay tạo cửa sổ mới
            stage.getScene().setRoot(newRoot);

        } catch (IOException e) {
            System.out.println("Lỗi: Không thể tìm thấy file AuctionDetail.fxml!");
            e.printStackTrace(); // In lỗi đỏ ra console để dễ bắt bệnh nếu sai đường dẫn
        }
    }
}
module AuctionSystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.swing;

    // Cấp quyền cho JavaFX khởi chạy class MainApp trong package này
    exports auction.client;

    // Cấp quyền cho thư viện FXML sử dụng Reflection để đọc file .fxml và liên kết với Controller
    opens auction.client to javafx.fxml;

    // Lưu ý: Sau này khi bạn viết code cho Controller, nếu file Controller nằm ở thư mục riêng,
    // bạn cần mở (opens) cả thư mục đó nữa nhé. Ví dụ:
    opens auction.client.controller to javafx.fxml;
}
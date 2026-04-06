package auction.client.controller;

// NHỚ KỸ: Chỉ import các thư viện bắt đầu bằng javafx...

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class DetailController {

    @FXML
    private TextField bidAmountField;

    @FXML
    public void placeBid(ActionEvent event) {

        String bidAmount = bidAmountField.getText();

        System.out.println("================================");
        System.out.println("Khách hàng vừa đặt giá: " + bidAmount + " USD");
        System.out.println("================================");
    }
}
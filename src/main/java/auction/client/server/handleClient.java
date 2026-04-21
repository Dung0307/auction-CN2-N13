package auction.client.server;

import auction.client.model.ManageUser;
import auction.client.model.User;
import auction.client.service.AuctionService;
import auction.client.service.ProductService;
import auction.client.model.Product;
import auction.client.exception.InvalidBidException;
import auction.client.exception.AuctionClosedException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class handleClient {
    private AuctionService auctionService = new AuctionService();
    private ProductService productService = new ProductService();

    public void handleClient(Socket socket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String request = in.readLine();

            String[] parts = request.split("\\|");

            if (parts[0].equals("BID")) {
                try {
                    int auctionId = Integer.parseInt(parts[1]);
                    int userId = Integer.parseInt(parts[2]);
                    double price = Double.parseDouble(parts[3]);

                    // Tìm Product từ auctionId
                    // Giả sử sản phẩm được lưu và có thể tìm được
                    // TODO: Implement cơ chế ánh xạ auctionId -> Product
                    Product product = productService.findProductByName("Product_" + auctionId);

                    if (product != null) {
                        // Gọi placeBid từ instance
                        auctionService.placeBid(product, price);
                        out.println("SUCCESS");
                    } else {
                        out.println("FAIL|Product not found");
                    }
                } catch (InvalidBidException e) {
                    out.println("FAIL|" + e.getMessage());
                } catch (AuctionClosedException e) {
                    out.println("FAIL|Auction is closed");
                }
            }
            if (parts[0].equals("LOGIN")) {
                try {
                    ManageUser manageUser = new ManageUser();
                    for (User pt: manageUser.getUserList()) {
                        if (pt.getName().equals(parts[1])
                        && pt.getPassword().equals(parts[2])) {
                            out.println("SUCCESS");
                            return;
                        }
                    }

                    out.println("FAIL|Có vẻ như bạn chưa đăng ký tài khoản trước đó");
                }
                catch (Exception e) {
                    out.println("FAIL|Hãy thử nhập lại");
                }
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

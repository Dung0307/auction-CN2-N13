package auction.client.server;

import java.net.ServerSocket;
import java.net.Socket;

public class AuctionServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(9999)) {
            System.out.println("🚀 Auction Server started on port 9999");

            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("📨 New client connected: " + client.getInetAddress());

                // Tạo instance của handleClient rồi gọi method
                handleClient clientHandler = new handleClient();
                new Thread(() -> clientHandler.handleClient(client)).start();
            }
        } catch (Exception e) {
            System.out.println("❌ Có lỗi rồi Đại vương ơi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

package auction.client.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * AuctionClient - Xử lý kết nối với server
 * Gửi request đặt giá đến AuctionServer
 */
public class AuctionClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 9999;
    private static final int TIMEOUT_MS = 5000; // 5 second timeout

    /**
     * Gửi request đặt giá đến server
     *
     * @param auctionId ID của sản phẩm đấu giá
     * @param userId    ID của người dùng
     * @param bidAmount Giá đặt
     * @return true nếu server trả về SUCCESS, false nếu FAIL
     */
    public static boolean sendBidToServer(int auctionId, int userId, double bidAmount) {
        Socket socket = null;
        try {
            // 1. Kết nối đến server
            System.out.println("🔌 Đang kết nối đến server " + SERVER_HOST + ":" + SERVER_PORT + "...");
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            socket.setSoTimeout(TIMEOUT_MS);
            System.out.println("✅ Kết nối thành công!");

            // 2. Tạo input/output stream
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 3. Format request: "BID|auctionId|userId|bidAmount"
            String request = "BID|" + auctionId + "|" + userId + "|" + bidAmount;
            System.out.println("📤 Gửi request: " + request);

            // 4. Gửi request
            out.println(request);
            System.out.println("✅ Request đã được gửi!");

            // 5. Đợi phản hồi từ server
            System.out.println("⏳ Đợi phản hồi từ server...");
            String response = in.readLine();

            if (response == null) {
                System.out.println("❌ Server không gửi lại response!");
                return false;
            }

            System.out.println("📥 Nhận phản hồi: " + response);

            // 7. Kiểm tra kết quả
            if (response.startsWith("SUCCESS")) {
                System.out.println("✅ Đặt giá thành công trên server!");
                return true;
            } else {
                System.out.println("❌ Server trả lại lỗi: " + response);
                return false;
            }

        } catch (java.net.ConnectException e) {
            System.err.println("❌ Lỗi kết nối: Server không chạy hoặc port bị chiếm");
            System.err.println("   Detail: " + e.getMessage());
            return false;

        } catch (java.net.SocketTimeoutException e) {
            System.err.println("❌ Lỗi timeout: Server không phản hồi trong " + TIMEOUT_MS + "ms");
            System.err.println("   Detail: " + e.getMessage());
            return false;

        } catch (Exception e) {
            System.err.println("❌ Lỗi kết nối server: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            return false;

        } finally {
            // 6. Đóng kết nối
            if (socket != null) {
                try {
                    socket.close();
                    System.out.println("🔌 Đóng kết nối");
                } catch (Exception e) {
                    System.err.println("⚠️ Lỗi đóng socket: " + e.getMessage());
                }
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////
    /* Dưới đây là phương thức để gửi yêu cầu login đến server */
    public static boolean sendLoginRequestToServer(String username, String password) {
        Socket socket = null;
        try {
            // 1. Kết nối đến server
            System.out.println("🔌 Đang kết nối đến server " + SERVER_HOST + ":" + SERVER_PORT + "...");
            socket = new Socket(SERVER_HOST, 9998);
            socket.setSoTimeout(TIMEOUT_MS);
            System.out.println("✅ Kết nối thành công!");

            // 2. Tạo input/output stream
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 3. Format request: "LOGIN|username|password"
            String request = "LOGIN|" + username + "|" + password;
            System.out.println("📤 Gửi request: " + request);

            // 4. Gửi request
            out.println(request);
            System.out.println("✅ Request đã được gửi!");

            // 5. Đợi phản hồi từ server
            System.out.println("⏳ Đợi phản hồi từ server...");
            String response = in.readLine();

            if (response == null) {
                System.out.println("❌ Server không gửi lại response!");
                return false;
            }

            System.out.println("📥 Nhận phản hồi: " + response);

            // 7. Kiểm tra kết quả
            if (response.startsWith("SUCCESS")) {
                System.out.println("✅ Đăng nhập thành công!");
                return true;
            } else {
                System.out.println("❌ Server trả lại lỗi: " + response);
                return false;
            }

        } catch (java.net.ConnectException e) {
            System.err.println("❌ Lỗi kết nối: Server không chạy hoặc port bị chiếm");
            System.err.println("   Detail: " + e.getMessage());
            return false;

        } catch (java.net.SocketTimeoutException e) {
            System.err.println("❌ Lỗi timeout: Server không phản hồi trong " + TIMEOUT_MS + "ms");
            System.err.println("   Detail: " + e.getMessage());
            return false;

        } catch (Exception e) {
            System.err.println("❌ Lỗi kết nối server: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}


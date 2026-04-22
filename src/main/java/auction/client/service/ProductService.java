package auction.client.service;

import auction.client.model.Product;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ProductService - Xử lý tất cả logic liên quan đến sản phẩm
 * Tách biệt business logic từ UI Controller
 */
public class ProductService {

    /**
     * Thêm sản phẩm mới vào kho
     * - Validate dữ liệu
     * - Tạo object Product
     * - Thêm vào danh sách toàn cầu
     */
    public void addProduct(String name, String price, String imageUrl, String desc)
            throws IllegalArgumentException {

        // Validate dữ liệu
        if (!isValidProduct(name, price, imageUrl)) {
            throw new IllegalArgumentException("Dữ liệu sản phẩm không hợp lệ!");
        }

        // Tạo và thêm vào kho
        Product newProduct = new Product(name, price, imageUrl, desc);
        Product.allProducts.add(newProduct);
        System.out.println("✅ Sản phẩm '" + name + "' đã được thêm thành công!");
    }

    /**
     * Validate thông tin sản phẩm
     */
    public boolean isValidProduct(String name, String price, String imageUrl) {
        // Kiểm tra các trường không rỗng
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        if (price == null || price.trim().isEmpty()) {
            return false;
        }
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return false;
        }

        // Kiểm tra price là số hợp lệ
        try {
            String cleanPrice = price.replaceAll("[^0-9]", "");
            double priceValue = Double.parseDouble(cleanPrice);
            if (priceValue <= 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        // Kiểm tra tên không trùng lặp
        for (Product product : Product.allProducts) {
            if (product.getName().equalsIgnoreCase(name.trim())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Lấy tất cả sản phẩm
     */
    public List<Product> getAllProducts() {
        return new ArrayList<>(Product.allProducts);
    }

    /**
     * Tìm sản phẩm theo từ khóa
     */
    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllProducts();
        }

        String searchTerm = keyword.toLowerCase().trim();
        return Product.allProducts.stream()
                .filter(p -> p.getName().toLowerCase().contains(searchTerm) ||
                            p.getDescription().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }

    /**
     * Lọc sản phẩm theo khoảng giá
     */
    public List<Product> filterByPrice(double minPrice, double maxPrice) {
        return Product.allProducts.stream()
                .filter(p -> {
                    try {
                        double price = Double.parseDouble(p.getPrice().replaceAll("[^0-9]", ""));
                        return price >= minPrice && price <= maxPrice;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Tải ảnh từ URL với fallback (ảnh mặc định nếu lỗi)
     */
    public Image loadImageWithFallback(String imageUrl, String fallbackUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return new Image(fallbackUrl);
        }

        try {
            Image image = new Image(imageUrl, true);
            image.errorProperty().addListener((obs, oldError, newError) -> {
                if (newError) {
                    System.out.println("⚠️ Không thể tải ảnh từ: " + imageUrl);
                }
            });
            return image;
        } catch (Exception e) {
            System.out.println("⚠️ Lỗi tải ảnh: " + e.getMessage());
            return new Image(fallbackUrl);
        }
    }

    /**
     * Kiểm tra URL ảnh có hợp lệ không
     */
    public boolean isValidImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return false;
        }

        try {
            new Image(imageUrl, true);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Lấy tổng số sản phẩm
     */
    public int getTotalProductCount() {
        return Product.allProducts.size();
    }

    /**
     * Tìm sản phẩm theo tên
     */
    public Product findProductByName(String name) {
        return Product.allProducts.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}


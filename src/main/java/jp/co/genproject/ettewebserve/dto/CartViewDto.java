package jp.co.genproject.ettewebserve.dto;

import java.time.LocalDateTime;

public class CartViewDto {
    private Integer cartId;
    private Integer productId;
    private String productName;
    private String imagePath;
    private Integer salePrice;
    private String sizeName;
    private Integer quantity;
    private LocalDateTime addedAt;
    private String addedAtFormatted;

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    public String getAddedAtFormatted() {
        return addedAtFormatted;
    }
    public void setAddedAtFormatted(String addedAtFormatted) {
        this.addedAtFormatted = addedAtFormatted;
    }
}

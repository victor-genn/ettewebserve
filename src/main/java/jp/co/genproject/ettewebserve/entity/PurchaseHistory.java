package jp.co.genproject.ettewebserve.entity;

import java.time.LocalDateTime;

public class PurchaseHistory {

    private Integer purchaseId;
    private Integer userId;
    private Integer productId;
    private Integer quantity;
    private Integer totalPrice;
    private LocalDateTime purchasedAt;

    public PurchaseHistory() {}

    public PurchaseHistory(Integer purchaseId, Integer userId, Integer productId,
                           Integer quantity, Integer totalPrice, LocalDateTime purchasedAt) {
        this.purchaseId = purchaseId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.purchasedAt = purchasedAt;
    }

    public Integer getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getPurchasedAt() {
        return purchasedAt;
    }

    public void setPurchasedAt(LocalDateTime purchasedAt) {
        this.purchasedAt = purchasedAt;
    }
}


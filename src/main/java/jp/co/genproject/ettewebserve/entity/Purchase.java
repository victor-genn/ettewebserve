package jp.co.genproject.ettewebserve.entity;

import java.time.LocalDateTime;

public class Purchase {
    private Integer purchaseId;
    private Integer userId;
    private LocalDateTime createdAt;

    public Purchase() {}

    public Purchase(Integer purchaseId, Integer userId, LocalDateTime createdAt) {
        this.purchaseId = purchaseId;
        this.userId = userId;
        this.createdAt = createdAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

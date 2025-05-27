package jp.co.genproject.ettewebserve.entity;

public class PurchaseDetail {
    private Integer purchaseDetailId;
    private Integer purchaseId;
    private Integer productId;
    private Integer sizeId;
    private Integer quantity;
    private Integer price;

    public PurchaseDetail() {}

    public PurchaseDetail(Integer purchaseDetailId, Integer purchaseId, Integer productId,
                          Integer sizeId, Integer quantity, Integer price) {
        this.purchaseDetailId = purchaseDetailId;
        this.purchaseId = purchaseId;
        this.productId = productId;
        this.sizeId = sizeId;
        this.quantity = quantity;
        this.price = price;
    }

    public Integer getPurchaseDetailId() {
        return purchaseDetailId;
    }

    public void setPurchaseDetailId(Integer purchaseDetailId) {
        this.purchaseDetailId = purchaseDetailId;
    }

    public Integer getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getSizeId() {
        return sizeId;
    }

    public void setSizeId(Integer sizeId) {
        this.sizeId = sizeId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}

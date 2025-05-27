package jp.co.genproject.ettewebserve.form;

import java.util.List;

public class PurchaseForm {
    private List<Integer> cartIds;
    private Integer totalQuantity;
    private Integer totalPrice;
    private Integer taxIncludedPrice;
    private Integer point;

    public List<Integer> getCartIds() {
        return cartIds;
    }

    public void setCartIds(List<Integer> cartIds) {
        this.cartIds = cartIds;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getTaxIncludedPrice() {
        return taxIncludedPrice;
    }

    public void setTaxIncludedPrice(Integer taxIncludedPrice) {
        this.taxIncludedPrice = taxIncludedPrice;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
}

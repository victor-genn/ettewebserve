package jp.co.genproject.ettewebserve.service;

public interface PurchaseService {

    // 購入登録
    public void register(Integer userId, Integer productId, Integer sizeId, Integer quantity, Integer price);
}

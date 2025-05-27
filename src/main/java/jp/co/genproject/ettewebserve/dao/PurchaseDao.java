package jp.co.genproject.ettewebserve.dao;

public interface PurchaseDao {

    // 購入登録
    public void register(Integer userId, Integer productId, Integer sizeId, Integer quantity, Integer price);
}

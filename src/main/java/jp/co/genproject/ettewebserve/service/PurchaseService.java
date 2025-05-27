package jp.co.genproject.ettewebserve.service;

import java.util.List;

import jp.co.genproject.ettewebserve.dto.CartViewDto;

public interface PurchaseService {

    // カート登録
    public void addToCart(Integer userId, Integer productId, Integer sizeId, Integer quantity);

    // ユーザーIDでカートリスト取得
    public List<CartViewDto> findCartViewByUserId(Integer userId);

    // カートリストの中で、購入する商品を選ぶ
    public List<CartViewDto> purchaseFromCart(Integer userId, List<Integer> cartIds);

    // 購入履歴登録
    public void registerPurchase(Integer userId, List<Integer> cartId, Integer totalQuantity, Integer totalPrice);
}

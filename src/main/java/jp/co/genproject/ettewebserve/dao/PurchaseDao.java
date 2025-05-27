package jp.co.genproject.ettewebserve.dao;

import java.util.List;

import jp.co.genproject.ettewebserve.dto.CartViewDto;
import jp.co.genproject.ettewebserve.entity.PurchaseHistory;

public interface PurchaseDao {

    // カート登録
    public void addToCart(Integer userId, Integer productId, Integer sizeId, Integer quantity);

    // ユーザーIDでカートリスト取得
    public List<CartViewDto> findCartViewByUserId(Integer userId);

    // カートリストの中で、購入する商品を選ぶ
    public List<CartViewDto> purchaseFromCart(Integer userId, List<Integer> cartIds);

    // 購入履歴登録
    public void registerPurchase(Integer userId, List<Integer> cartId, Integer totalQuantity, Integer totalPrice);

    // 全件取得
    public List<PurchaseHistory> findPurchaseByAll();

    // 商品IDで検索
    public List<PurchaseHistory> findPurchaseByProducName(String productName);

    // 金額で検索
    public List<PurchaseHistory> findPurchaseByPrice(Integer price);

    // 日付順に並び替え
    public List<PurchaseHistory> sortPurchaseByDate();

    // 数量順に並び替え
    public List<PurchaseHistory> sortPurchaseByQuantity();

    // 金額順に並び替え
    public List<PurchaseHistory> sortPurchaseByPrice();

    // ユーザーIDで購入履歴を取得（フィルター用）
    public List<PurchaseHistory> findPurchaseByUserId(Integer userId);
}

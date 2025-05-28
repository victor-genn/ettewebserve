package jp.co.genproject.ettewebserve.dao;

import java.util.List;

import jp.co.genproject.ettewebserve.dto.CartViewDto;
import jp.co.genproject.ettewebserve.entity.PurchaseHistory;

public interface PurchaseDao {

    /** カートに商品を追加する */
    void addToCart(Integer userId, Integer productId, Integer sizeId, Integer quantity);

    /** ユーザーIDからカート一覧を取得する */
    List<CartViewDto> findCartViewByUserId(Integer userId);

    /** カート内から購入対象の商品を選択して取得する */
    List<CartViewDto> purchaseFromCart(Integer userId, List<Integer> cartIds);

    /** カート情報を元に購入履歴を登録する（在庫更新とカート削除含む） */
    void registerPurchase(Integer userId, List<Integer> cartId, Integer totalQuantity, Integer totalPrice);

    /** 購入履歴をIDリストで削除する */
    void deletePurchaseHistories(List<Integer> purchaseIds);

    /** 全購入履歴を取得する（降順ソート） */
    List<PurchaseHistory> findPurchaseByAll();

    /** 商品名で購入履歴を検索する */
    List<PurchaseHistory> findPurchaseByProducName(String productName);

    /** 金額で購入履歴を検索する */
    List<PurchaseHistory> findPurchaseByPrice(Integer price);

    /** 購入履歴を日付順で並び替える（新→旧） */
    List<PurchaseHistory> sortPurchaseByDate();

    /** 購入履歴を数量順で並び替える（多→少） */
    List<PurchaseHistory> sortPurchaseByQuantity();

    /** 購入履歴を金額順で並び替える（高→低） */
    List<PurchaseHistory> sortPurchaseByPrice();

    /** ユーザーIDから購入履歴を取得する（フィルター用） */
    List<PurchaseHistory> findPurchaseByUserId(Integer userId);
}
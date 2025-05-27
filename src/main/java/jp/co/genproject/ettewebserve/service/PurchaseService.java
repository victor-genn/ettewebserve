package jp.co.genproject.ettewebserve.service;

import java.util.List;

import jp.co.genproject.ettewebserve.dto.CartViewDto;
import jp.co.genproject.ettewebserve.entity.PurchaseHistory;

/**
 * 購入関連機能のサービスインターフェースクラス。
 * カート操作、購入処理、購入履歴の取得・検索・並び替えなど、
 * 購入に関するビジネスロジックを定義する。
 *
 * 主な機能：
 * ・カートへの商品追加およびカートリスト取得  
 * ・購入対象商品の選定および購入登録  
 * ・購入履歴の全件取得・検索（商品ID、金額、ユーザーID）  
 * ・購入履歴の並び替え（日時、数量、金額）
 *
 * 使用技術：
 * ・Spring Framework  
 * ・DTO/Entityによるデータ移送  
 * ・サービス層によるロジック分離
 *
 * @author 張勝現
 * @version 1.0
 */
public interface PurchaseService {

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

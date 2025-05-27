package jp.co.genproject.ettewebserve.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import jp.co.genproject.ettewebserve.dao.PurchaseDao;
import jp.co.genproject.ettewebserve.dto.CartViewDto;
import jp.co.genproject.ettewebserve.entity.PurchaseHistory;
import jp.co.genproject.ettewebserve.service.PurchaseService;

/**
 * 購入関連機能のサービス実装クラス。
 * カートへの商品追加、購入処理、購入履歴の取得・検索・並び替えなど、
 * 購入に関するビジネスロジックを実装する。
 *
 * 主な機能：
 * ・ユーザーIDによるカートリストの取得とカートへの登録  
 * ・選択したカート商品の購入処理および履歴登録  
 * ・購入履歴の全件取得・検索（商品ID・金額・ユーザーID）  
 * ・購入履歴の並び替え（日付順・数量順・金額順）
 *
 * 使用技術：
 * ・Spring Framework  
 * ・DI（依存性注入）  
 * ・Service/DAO分離アーキテクチャ
 *
 * @author 張勝現
 * @version 1.0
 */
@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseDao purchaseDao;

    public PurchaseServiceImpl(PurchaseDao purchaseDao) {
        this.purchaseDao = purchaseDao;
    }

    // カート登録
    public void addToCart(Integer userId, Integer productId, Integer sizeId, Integer quantity) {
        purchaseDao.addToCart(userId, productId, sizeId, quantity);
    }

    // ユーザーIDでカートリスト取得
    public List<CartViewDto> findCartViewByUserId(Integer userId){
        return purchaseDao.findCartViewByUserId(userId);
    }

    // カートリストの中で、購入する商品を選ぶ
    public List<CartViewDto> purchaseFromCart(Integer userId, List<Integer> cartIds){
        return purchaseDao.purchaseFromCart(userId, cartIds);
    }

    // 購入履歴登録
    public void registerPurchase(Integer userId, List<Integer> cartId, Integer totalQuantity, Integer totalPrice){
        purchaseDao.registerPurchase(userId, cartId, totalQuantity, totalPrice);
    }

        // 全件取得
    public List<PurchaseHistory> findPurchaseByAll(){
        return purchaseDao.findPurchaseByAll();
    }

    // 商品IDで検索
    public List<PurchaseHistory> findPurchaseByProducName(String productName){
        return purchaseDao.findPurchaseByProducName(productName);
    }

    // 金額で検索
    public List<PurchaseHistory> findPurchaseByPrice(Integer price){
        return purchaseDao.findPurchaseByPrice(price);
    }

    // 日付順に並び替え
    public List<PurchaseHistory> sortPurchaseByDate(){
        return purchaseDao.sortPurchaseByDate();
    }

    // 数量順に並び替え
    public List<PurchaseHistory> sortPurchaseByQuantity(){
        return purchaseDao.sortPurchaseByQuantity();
    }

    // 金額順に並び替え
    public List<PurchaseHistory> sortPurchaseByPrice(){
        return purchaseDao.sortPurchaseByPrice();
    }

    // ユーザーIDで購入履歴を取得（フィルター用）
    public List<PurchaseHistory> findPurchaseByUserId(Integer userId){
        return purchaseDao.findPurchaseByUserId(userId);
    }

}

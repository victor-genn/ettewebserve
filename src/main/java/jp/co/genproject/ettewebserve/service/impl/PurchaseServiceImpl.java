package jp.co.genproject.ettewebserve.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import jp.co.genproject.ettewebserve.dao.PurchaseDao;
import jp.co.genproject.ettewebserve.dto.CartViewDto;
import jp.co.genproject.ettewebserve.entity.PurchaseHistory;
import jp.co.genproject.ettewebserve.service.PurchaseService;

/**
 * 購入・カート機能のサービス実装クラス。
 * カートへの追加、購入処理、履歴管理のビジネスロジックを提供する。
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

    @Override
    public void addToCart(Integer userId, Integer productId, Integer sizeId, Integer quantity) {
        purchaseDao.addToCart(userId, productId, sizeId, quantity);
    }

    @Override
    public List<CartViewDto> findCartViewByUserId(Integer userId) {
        return purchaseDao.findCartViewByUserId(userId);
    }

    @Override
    public List<CartViewDto> purchaseFromCart(Integer userId, List<Integer> cartIds) {
        return purchaseDao.purchaseFromCart(userId, cartIds);
    }

    @Override
    public void registerPurchase(Integer userId, List<Integer> cartId, Integer totalQuantity, Integer totalPrice) {
        purchaseDao.registerPurchase(userId, cartId, totalQuantity, totalPrice);
    }

    @Override
    public void deletePurchaseHistories(List<Integer> purchaseIds) {
        purchaseDao.deletePurchaseHistories(purchaseIds);
    }

    @Override
    public List<PurchaseHistory> findPurchaseByAll() {
        return purchaseDao.findPurchaseByAll();
    }

    @Override
    public List<PurchaseHistory> findPurchaseByProducName(String productName) {
        return purchaseDao.findPurchaseByProducName(productName);
    }

    @Override
    public List<PurchaseHistory> findPurchaseByPrice(Integer price) {
        return purchaseDao.findPurchaseByPrice(price);
    }

    @Override
    public List<PurchaseHistory> sortPurchaseByDate() {
        return purchaseDao.sortPurchaseByDate();
    }

    @Override
    public List<PurchaseHistory> sortPurchaseByQuantity() {
        return purchaseDao.sortPurchaseByQuantity();
    }

    @Override
    public List<PurchaseHistory> sortPurchaseByPrice() {
        return purchaseDao.sortPurchaseByPrice();
    }

    @Override
    public List<PurchaseHistory> findPurchaseByUserId(Integer userId) {
        return purchaseDao.findPurchaseByUserId(userId);
    }
}
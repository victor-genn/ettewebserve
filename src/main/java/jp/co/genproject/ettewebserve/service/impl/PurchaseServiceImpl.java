package jp.co.genproject.ettewebserve.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import jp.co.genproject.ettewebserve.dao.PurchaseDao;
import jp.co.genproject.ettewebserve.dto.CartViewDto;
import jp.co.genproject.ettewebserve.service.PurchaseService;

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
}

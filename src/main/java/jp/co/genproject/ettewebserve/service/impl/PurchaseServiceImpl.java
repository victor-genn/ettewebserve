package jp.co.genproject.ettewebserve.service.impl;

import org.springframework.stereotype.Service;

import jp.co.genproject.ettewebserve.dao.PurchaseDao;
import jp.co.genproject.ettewebserve.service.PurchaseService;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseDao purchaseDao;

    public PurchaseServiceImpl(PurchaseDao purchaseDao) {
        this.purchaseDao = purchaseDao;
    }

    public void register(Integer userId, Integer productId, Integer sizeId, Integer quantity, Integer price) {
        purchaseDao.register(userId, productId, sizeId, quantity, price);
    }
}

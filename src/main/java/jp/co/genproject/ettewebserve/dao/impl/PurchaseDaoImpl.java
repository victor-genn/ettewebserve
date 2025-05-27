package jp.co.genproject.ettewebserve.dao.impl;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import jp.co.genproject.ettewebserve.dao.PurchaseDao;

@Repository
public class PurchaseDaoImpl implements PurchaseDao {

    private final NamedParameterJdbcTemplate template;

    private static final String SQL_INSERT_PURCHASE = "INSERT INTO purchase (user_id) VALUES (:userId)";
    private static final String SQL_INSERT_PURCHASE_DETAIL = "INSERT INTO purchase_detail (purchase_id, product_id, size_id, quantity, price) VALUES (:purchaseId, :productId, :sizeId, :quantity, :price)";
    private static final String SQL_SELECT_LAST_ID = "SELECT LAST_INSERT_ID()";

    public PurchaseDaoImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    // 購入登録
    public void register(Integer userId, Integer productId, Integer sizeId, Integer quantity, Integer price) {
        
        // 購入履歴生成
        MapSqlParameterSource purchaseParam = new MapSqlParameterSource();
        purchaseParam.addValue("userId", userId);
        template.update(SQL_INSERT_PURCHASE, purchaseParam);

        // 購入履歴ID取得
        Integer purchaseId = template.queryForObject(SQL_SELECT_LAST_ID, new MapSqlParameterSource(), Integer.class);
        
        // 購入詳細に購入履歴IDおよび情報入力
        MapSqlParameterSource detailParam = new MapSqlParameterSource();
        detailParam.addValue("purchaseId", purchaseId);
        detailParam.addValue("productId", productId);
        detailParam.addValue("sizeId", sizeId);
        detailParam.addValue("quantity", quantity);
        detailParam.addValue("price", price);

        // 購入登録
        template.update(SQL_INSERT_PURCHASE_DETAIL, detailParam);
    }
}


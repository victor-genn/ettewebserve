package jp.co.genproject.ettewebserve.dao.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import jp.co.genproject.ettewebserve.dao.PurchaseDao;
import jp.co.genproject.ettewebserve.dto.CartViewDto;
import jp.co.genproject.ettewebserve.entity.PurchaseHistory;

@Repository
public class PurchaseDaoImpl implements PurchaseDao {

    private final NamedParameterJdbcTemplate template;
    private final BeanPropertyRowMapper<CartViewDto> cartRowMapper = new BeanPropertyRowMapper<CartViewDto>(CartViewDto.class);
    LocalDateTime now = LocalDateTime.now();

    private static final String SQL_INSERT_CART = "INSERT INTO cart (user_id, product_id, size_id, quantity, added_at) VALUES (:userId, :productId, :sizeId, :quantity, NOW())";
    private static final String SQL_FIND_CART_VIEW_BY_USER = "SELECT c.cart_id, c.product_id, p.product_name, p.imagePath, p.sale_price, s.size_name, c.quantity, c.added_at FROM cart c JOIN product p ON c.product_id = p.product_id JOIN size s ON c.size_id = s.size_id WHERE c.user_id = :userId ORDER BY c.added_at DESC;";
    private static final String SQL_SELECT_CART_BY_CART_ID = "SELECT c.cart_id, p.product_name, p.imagePath, p.sale_price, c.size_id, c.quantity, c.added_at, s.size_name FROM cart c JOIN product p ON c.product_id = p.product_id JOIN size s ON c.size_id = s.size_id WHERE c.user_id = :userId AND c.cart_id IN (:cartIds)";
    private static final String SQL_INSERT_PURCHASE_HISTORY = "INSERT INTO purchase_history (user_id, product_id, quantity, total_price, purchased_at) VALUES (:userId, :productId, :quantity, :totalPrice, :purchasedAt)";
    private static final String SQL_SELECT_CART_ITEM = "SELECT product_id, quantity, sale_price FROM cart WHERE cart_id = :cartId";
    private static final String SQL_DELETE_CART_ITEM = "DELETE FROM cart WHERE cart_id = :cartId";

    public PurchaseDaoImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    // カート登録
    public void addToCart(Integer userId, Integer productId, Integer sizeId, Integer quantity) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("userId", userId);
        param.addValue("productId", productId);
        param.addValue("sizeId", sizeId);
        param.addValue("quantity", quantity);

        template.update(SQL_INSERT_CART, param);
    }

    // ユーザーIDでカートリスト取得
    public List<CartViewDto> findCartViewByUserId(Integer userId) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("userId", userId);
        return template.query(SQL_FIND_CART_VIEW_BY_USER, param, cartRowMapper);
    }

    // カートリストの中で、購入する商品を選ぶ
    public List<CartViewDto> purchaseFromCart(Integer userId, List<Integer> cartIds){
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("userId", userId);
        param.addValue("cartIds", cartIds);
        return template.query(SQL_SELECT_CART_BY_CART_ID, param, cartRowMapper);
    }

    public void registerPurchase(Integer userId, List<Integer> cartIdList, Integer totalQuantity, Integer totalPrice) {
        LocalDateTime now = LocalDateTime.now();

        for (Integer cartId : cartIdList) {
            MapSqlParameterSource selectParam = new MapSqlParameterSource().addValue("cartId", cartId);
            Map<String, Object> cartItem = template.queryForMap(SQL_SELECT_CART_ITEM, selectParam);

            Integer productId = (Integer) cartItem.get("product_id");
            Integer quantity = (Integer) cartItem.get("quantity");
            Integer price = (Integer) cartItem.get("sale_price");
            Integer itemTotal = price * quantity;

            MapSqlParameterSource insertParam = new MapSqlParameterSource();
            insertParam.addValue("userId", userId);
            insertParam.addValue("productId", productId);
            insertParam.addValue("quantity", quantity);
            insertParam.addValue("totalPrice", itemTotal);
            insertParam.addValue("purchasedAt", now);

            template.update(SQL_INSERT_PURCHASE_HISTORY, insertParam);

            template.update(SQL_DELETE_CART_ITEM, selectParam);
        }
    }

    // 購入履歴検索および出力
    public List<PurchaseHistory> findPurchaseByAll(){}

    public List<PurchaseHistory> findPurchaseByProductId(Integer productId){}

    public List<PurchaseHistory> findPurchaseByPrice(Integer Price){}

    public List<PurchaseHistory> sortPurchaseByDate(){}

    public List<PurchaseHistory> sortPurchaseByQuantity(){}

    public List<PurchaseHistory> sortPurchaseByPrice(){}
}
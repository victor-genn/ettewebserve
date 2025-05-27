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
    private final BeanPropertyRowMapper<PurchaseHistory> PurchaseRowMapper = new BeanPropertyRowMapper<>(PurchaseHistory.class);
    LocalDateTime now = LocalDateTime.now();

    private static final String SQL_INSERT_CART = "INSERT INTO cart (user_id, product_id, size_id, quantity, added_at) VALUES (:userId, :productId, :sizeId, :quantity, NOW())";
    private static final String SQL_FIND_CART_VIEW_BY_USER = "SELECT c.cart_id, c.product_id, p.product_name, p.imagePath, p.sale_price, s.size_name, c.quantity, c.added_at FROM cart c JOIN product p ON c.product_id = p.product_id JOIN size s ON c.size_id = s.size_id WHERE c.user_id = :userId ORDER BY c.added_at DESC;";
    private static final String SQL_SELECT_CART_BY_CART_ID = "SELECT c.cart_id, p.product_name, p.imagePath, p.sale_price, c.size_id, c.quantity, c.added_at, s.size_name FROM cart c JOIN product p ON c.product_id = p.product_id JOIN size s ON c.size_id = s.size_id WHERE c.user_id = :userId AND c.cart_id IN (:cartIds)";
    private static final String SQL_INSERT_PURCHASE_HISTORY = "INSERT INTO purchase_history (user_id, product_id, quantity, total_price, purchased_at) VALUES (:userId, :productId, :quantity, :totalPrice, :purchasedAt)";
    private static final String SQL_SELECT_CART_ITEM = "SELECT product_id, quantity, sale_price FROM cart WHERE cart_id = :cartId";
    private static final String SQL_DELETE_CART_ITEM = "DELETE FROM cart WHERE cart_id = :cartId";
    private static final String SQL_SELECT_PURCHASE_BY_USERID = "SELECT * FROM purchase_history WHERE user_id = :userId ORDER BY purchased_at DESC";
    private static final String SQL_SELECT_PURCHASE_BY_DATE = "SELECT * FROM purchase_history ORDER BY purchased_at DESC";
    private static final String SQL_SELECT_PURCHASE_BY_PRODUCTNAME = "SELECT ph.* FROM purchase_history ph JOIN product p ON ph.product_id = p.product_id WHERE p.product_name LIKE CONCAT('%', :productName, '%') ORDER BY ph.purchased_at DESC";
    private static final String SQL_SELECT_PURCHASE_BY_PRICE = "SELECT * FROM purchase_history WHERE total_price = :price";
    private static final String SQL_SORT_BY_DATE = "SELECT * FROM purchase_history ORDER BY purchased_at DESC";
    private static final String SQL_SORT_BY_QUANTITY = "SELECT * FROM purchase_history ORDER BY quantity DESC";
    private static final String SQL_SORT_BY_PRICE = "SELECT * FROM purchase_history ORDER BY total_price DESC";
    

    public PurchaseDaoImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    // カート登録
    @Override
    public void addToCart(Integer userId, Integer productId, Integer sizeId, Integer quantity) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("userId", userId);
        param.addValue("productId", productId);
        param.addValue("sizeId", sizeId);
        param.addValue("quantity", quantity);

        template.update(SQL_INSERT_CART, param);
    }

    // ユーザーIDでカートリスト取得
    @Override
    public List<CartViewDto> findCartViewByUserId(Integer userId) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("userId", userId);
        return template.query(SQL_FIND_CART_VIEW_BY_USER, param, cartRowMapper);
    }

    // カートリストの中で、購入する商品を選ぶ
    @Override
    public List<CartViewDto> purchaseFromCart(Integer userId, List<Integer> cartIds){
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("userId", userId);
        param.addValue("cartIds", cartIds);
        return template.query(SQL_SELECT_CART_BY_CART_ID, param, cartRowMapper);
    }

    // 購入登録
    @Override
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

    // 全件取得
    @Override
    public List<PurchaseHistory> findPurchaseByAll() {
        return template.query(SQL_SELECT_PURCHASE_BY_DATE, PurchaseRowMapper);
    }

    // 商品IDで検索
    @Override
    public List<PurchaseHistory> findPurchaseByProducName(String productName) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productName", productName);
        return template.query(SQL_SELECT_PURCHASE_BY_PRODUCTNAME, params, PurchaseRowMapper);
    }

    // 金額で検索
    @Override
    public List<PurchaseHistory> findPurchaseByPrice(Integer price) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("price", price);
        return template.query(SQL_SELECT_PURCHASE_BY_PRICE, params, PurchaseRowMapper);
    }

    // 日付順に並び替え
    @Override
    public List<PurchaseHistory> sortPurchaseByDate() {
        return template.query(SQL_SORT_BY_DATE, PurchaseRowMapper);
    }

    // 数量順に並び替え
    @Override
    public List<PurchaseHistory> sortPurchaseByQuantity() {
        return template.query(SQL_SORT_BY_QUANTITY, PurchaseRowMapper);
    }

    // 金額順に並び替え
    @Override
    public List<PurchaseHistory> sortPurchaseByPrice() {
        return template.query(SQL_SORT_BY_PRICE, PurchaseRowMapper);
    }

    // ユーザーIDで購入履歴を取得（フィルター用）
    @Override
    public List<PurchaseHistory> findPurchaseByUserId(Integer userId) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("userId", userId);
        return template.query(SQL_SELECT_PURCHASE_BY_USERID, param, PurchaseRowMapper);
    }
}
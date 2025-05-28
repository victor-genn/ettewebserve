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

/**
 * 購入・カート関連機能のDAO実装クラス。
 * カートへの商品追加、カート一覧表示、購入処理、購入履歴管理など、
 * 購入フロー全体に関するデータベース操作を実装する。
 *
 * <p><b>主な機能：</b></p>
 * <ul>
 *   <li>カートへの商品追加・一覧取得・購入対象取得</li>
 *   <li>カート情報に基づく購入登録（在庫更新・カート削除含む）</li>
 *   <li>購入履歴の取得・検索・並び替え・削除</li>
 * </ul>
 *
 * <p><b>使用技術：</b></p>
 * <ul>
 *   <li>Spring JDBC</li>
 *   <li>NamedParameterJdbcTemplate</li>
 *   <li>BeanPropertyRowMapper</li>
 * </ul>
 *
 * @author 張勝現
 * @version 1.0
 */
@Repository
public class PurchaseDaoImpl implements PurchaseDao {
    // JDBC
    private final NamedParameterJdbcTemplate template;

    // RowMapper
    private final BeanPropertyRowMapper<CartViewDto> cartRowMapper = new BeanPropertyRowMapper<>(CartViewDto.class);
    private final BeanPropertyRowMapper<PurchaseHistory> PurchaseRowMapper = new BeanPropertyRowMapper<>(PurchaseHistory.class);

    // SQL文
    private static final String SQL_INSERT_CART = "INSERT INTO cart (user_id, product_id, size_id, quantity, added_at) VALUES (:userId, :productId, :sizeId, :quantity, NOW())";
    private static final String SQL_FIND_CART_VIEW_BY_USER = "SELECT c.cart_id, c.product_id, p.product_name, p.image_path, p.sale_price, s.size_name, c.quantity, c.added_at FROM cart c JOIN product p ON c.product_id = p.product_id JOIN size s ON c.size_id = s.size_id WHERE c.user_id = :userId ORDER BY c.added_at DESC";
    private static final String SQL_SELECT_CART_BY_CART_ID = "SELECT c.cart_id, p.product_name, p.image_path, p.sale_price, c.size_id, c.quantity, c.added_at, s.size_name FROM cart c JOIN product p ON c.product_id = p.product_id JOIN size s ON c.size_id = s.size_id WHERE c.user_id = :userId AND c.cart_id IN (:cartIds)";
    private static final String SQL_SELECT_CART_ITEM = "SELECT c.product_id, c.quantity, p.sale_price, c.size_id FROM cart c JOIN product p ON c.product_id = p.product_id WHERE c.cart_id = :cartId";
    private static final String SQL_DELETE_CART_ITEM = "DELETE FROM cart WHERE cart_id = :cartId";
    private static final String SQL_INSERT_PURCHASE_HISTORY = "INSERT INTO purchase_history (user_id, product_id, quantity, total_price, purchased_at) VALUES (:userId, :productId, :quantity, :totalPrice, :purchasedAt)";
    private static final String SQL_DELETE_PURCHASE = "DELETE FROM purchase_history WHERE purchase_id IN (:purchaseIds)";
    private static final String SQL_SELECT_PURCHASE_BY_USERID = "SELECT * FROM purchase_history WHERE user_id = :userId ORDER BY purchased_at DESC";
    private static final String SQL_SELECT_PURCHASE_BY_DATE = "SELECT * FROM purchase_history ORDER BY purchased_at DESC";
    private static final String SQL_SELECT_PURCHASE_BY_PRODUCTNAME = "SELECT ph.* FROM purchase_history ph JOIN product p ON ph.product_id = p.product_id WHERE p.product_name LIKE :productName ESCAPE '\\\\' ORDER BY ph.purchased_at DESC";
    private static final String SQL_SELECT_PURCHASE_BY_PRICE = "SELECT * FROM purchase_history WHERE total_price = :price";
    private static final String SQL_SORT_BY_DATE = "SELECT * FROM purchase_history ORDER BY purchased_at DESC";
    private static final String SQL_SORT_BY_QUANTITY = "SELECT * FROM purchase_history ORDER BY quantity DESC";
    private static final String SQL_SORT_BY_PRICE = "SELECT * FROM purchase_history ORDER BY total_price DESC";
    private static final String SQL_UPDATE_STOCK = "UPDATE product SET stock_s = CASE WHEN :sizeId = 1 THEN stock_s - :quantity ELSE stock_s END, stock_m = CASE WHEN :sizeId = 2 THEN stock_m - :quantity ELSE stock_m END, stock_l = CASE WHEN :sizeId = 3 THEN stock_l - :quantity ELSE stock_l END, stock_xl = CASE WHEN :sizeId = 4 THEN stock_xl - :quantity ELSE stock_xl END WHERE product_id = :productId";

    // コンストラクター
    public PurchaseDaoImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    /** カートに商品を追加する */
    @Override
    public void addToCart(Integer userId, Integer productId, Integer sizeId, Integer quantity) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("userId", userId);
        param.addValue("productId", productId);
        param.addValue("sizeId", sizeId);
        param.addValue("quantity", quantity);
        template.update(SQL_INSERT_CART, param);
    }

    /** ユーザーIDからカート一覧を取得する */
    @Override
    public List<CartViewDto> findCartViewByUserId(Integer userId) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("userId", userId);
        return template.query(SQL_FIND_CART_VIEW_BY_USER, param, cartRowMapper);
    }

    /** 購入対象のカートデータを取得する */
    @Override
    public List<CartViewDto> purchaseFromCart(Integer userId, List<Integer> cartIds) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("userId", userId);
        param.addValue("cartIds", cartIds);
        return template.query(SQL_SELECT_CART_BY_CART_ID, param, cartRowMapper);
    }

    /** 
     * カート情報を元に購入履歴を登録する  
     * 在庫更新と購入後のカート削除処理も含む
     */
    @Override
    public void registerPurchase(Integer userId, List<Integer> cartIdList, Integer totalQuantity, Integer totalPrice) {
        LocalDateTime now = LocalDateTime.now();

        for (Integer cartId : cartIdList) {
            MapSqlParameterSource selectParam = new MapSqlParameterSource().addValue("cartId", cartId);
            Map<String, Object> cartItem;
            try {
                cartItem = template.queryForMap(SQL_SELECT_CART_ITEM, selectParam);
            } catch (Exception e) {
                continue;
            }

            Integer productId = (Integer) cartItem.get("product_id");
            Integer quantity = (Integer) cartItem.get("quantity");
            Integer salePrice = (Integer) cartItem.get("sale_price");
            Integer sizeId = (Integer) cartItem.get("size_id");

            if (quantity == null || quantity <= 0)
                continue;

            Integer itemTotal = salePrice * quantity;

            MapSqlParameterSource insertParam = new MapSqlParameterSource()
                    .addValue("userId", userId)
                    .addValue("productId", productId)
                    .addValue("quantity", quantity)
                    .addValue("totalPrice", itemTotal)
                    .addValue("purchasedAt", now);

            MapSqlParameterSource updateStockParam = new MapSqlParameterSource()
                    .addValue("sizeId", sizeId)
                    .addValue("quantity", quantity)
                    .addValue("productId", productId);

            template.update(SQL_UPDATE_STOCK, updateStockParam);
            template.update(SQL_INSERT_PURCHASE_HISTORY, insertParam);
            template.update(SQL_DELETE_CART_ITEM, selectParam);
        }
    }

    /** 購入履歴をIDリストで削除する */
    @Override
    public void deletePurchaseHistories(List<Integer> purchaseIds) {
        if (purchaseIds == null || purchaseIds.isEmpty())
            return;
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("purchaseIds", purchaseIds);
        template.update(SQL_DELETE_PURCHASE, param);
    }

    /** 全購入履歴を取得する（降順ソート） */
    @Override
    public List<PurchaseHistory> findPurchaseByAll() {
        return template.query(SQL_SELECT_PURCHASE_BY_DATE, PurchaseRowMapper);
    }

    /** 商品名で購入履歴を検索する */
    @Override
    public List<PurchaseHistory> findPurchaseByProducName(String productName) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productName", "%" + escapeLike(productName) + "%");
        return template.query(SQL_SELECT_PURCHASE_BY_PRODUCTNAME, params, PurchaseRowMapper);
    }

    /** 金額で購入履歴を検索する */
    @Override
    public List<PurchaseHistory> findPurchaseByPrice(Integer price) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("price", price);
        return template.query(SQL_SELECT_PURCHASE_BY_PRICE, params, PurchaseRowMapper);
    }

    /** 日付順で購入履歴を並び替える（新→旧） */
    @Override
    public List<PurchaseHistory> sortPurchaseByDate() {
        return template.query(SQL_SORT_BY_DATE, PurchaseRowMapper);
    }

    /** 数量順で購入履歴を並び替える（多→少） */
    @Override
    public List<PurchaseHistory> sortPurchaseByQuantity() {
        return template.query(SQL_SORT_BY_QUANTITY, PurchaseRowMapper);
    }

    /** 金額順で購入履歴を並び替える（高→低） */
    @Override
    public List<PurchaseHistory> sortPurchaseByPrice() {
        return template.query(SQL_SORT_BY_PRICE, PurchaseRowMapper);
    }

    /** ユーザーIDによる購入履歴を取得する */
    @Override
    public List<PurchaseHistory> findPurchaseByUserId(Integer userId) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("userId", userId);
        return template.query(SQL_SELECT_PURCHASE_BY_USERID, param, PurchaseRowMapper);
    }

    // SQL文設定
    private String escapeLike(String keyword) {
        return keyword.replace("\\", "\\\\").replace("_", "\\_").replace("%", "\\%");
    }
}

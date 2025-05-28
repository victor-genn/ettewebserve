package jp.co.genproject.ettewebserve.dao.impl;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import jp.co.genproject.ettewebserve.dao.ProductDao;
import jp.co.genproject.ettewebserve.dto.ProductDto;
import jp.co.genproject.ettewebserve.entity.Category;
import jp.co.genproject.ettewebserve.entity.Country;
import jp.co.genproject.ettewebserve.entity.Keyword;
import jp.co.genproject.ettewebserve.entity.Product;

/**
 * 商品関連機能のDAO実装クラス。
 * 商品一覧取得、検索、詳細表示、推薦商品の取得、商品登録・更新など、
 * 商品に関するあらゆるデータベース操作を実装する。
 *
 * <p><b>主な機能：</b></p>
 * <ul>
 *   <li>商品一覧の取得および並び替え</li>
 *   <li>商品名・カテゴリ・キーワードによる検索</li>
 *   <li>推薦商品および商品詳細の取得</li>
 *   <li>商品登録・更新処理</li>
 *   <li>付随情報（カテゴリ名、キーワード名、国名）の取得</li>
 *   <li>並び順のバリデーションチェック（SQLインジェクション対策）</li>
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
public class ProductDaoImpl implements ProductDao {
    // JDBC
    private final NamedParameterJdbcTemplate template;

    // RowMapper
    private final BeanPropertyRowMapper<Category> categoryRowMapper = new BeanPropertyRowMapper<>(Category.class);
    private final BeanPropertyRowMapper<Keyword> keywordRowMapper = new BeanPropertyRowMapper<>(Keyword.class);
    private final BeanPropertyRowMapper<Country> countryRowMapper = new BeanPropertyRowMapper<>(Country.class);
    private final BeanPropertyRowMapper<Product> rowMapper = new BeanPropertyRowMapper<>(Product.class);

    // SQL文
    private static final String SQL_SELECT_ALL = "SELECT * FROM product ORDER BY product_id";
    private static final String SQL_SELECT_PRODUCT_ID_BY_NAME = "SELECT product_id FROM product WHERE product_name = :productName";
    private static final String SQL_SELECT_BY_SORT = "SELECT * FROM product ORDER BY ";
    private static final String SQL_SELECT_BY_CATEGORY = "SELECT * FROM product WHERE category_id = :categoryId ORDER BY ";
    private static final String SQL_SELECT_BY_PRODUCTNAME = "SELECT * FROM product WHERE product_name LIKE :productName ESCAPE '\\\\' ORDER BY ";
    private static final String SQL_SELECT_BY_KEYWORDID = "SELECT * FROM product WHERE keyword_id = :keywordId ORDER BY ";
    private static final String SQL_SELECT_BY_CATEGORY_PRODUCTNAME = "SELECT * FROM product WHERE category_id = :categoryId AND product_name LIKE :productName ESCAPE '\\\\' ORDER BY ";
    private static final String SQL_SELECT_BY_CATEGORY_KEYWORDID = "SELECT * FROM product WHERE category_id = :categoryId AND keyword_id = :keywordId ORDER BY ";
    private static final String SQL_SELECT_BY_PRODUCTID = "SELECT * FROM product WHERE product_id = :productId ORDER BY product_id";
    private static final String SQL_SELECT_BY_RECOMMENDS = "SELECT p.product_id, p.product_name, p.regular_price, p.sale_price, p.image_path FROM recommends r JOIN product p ON r.product_id = p.product_id";
    private static final String SQL_SELECT_ALL_CATEGORY = "SELECT * FROM category ORDER BY category_id";
    private static final String SQL_SELECT_ALL_KEYWORD = "SELECT * FROM keyword ORDER BY keyword_id";
    private static final String SQL_SELECT_ALL_COUNTRY = "SELECT * FROM country ORDER BY country_id";
    private static final String SQL_SELECT_BY_KEYWORD = "SELECT * FROM keyword WHERE keyword_name LIKE :keywordName ESCAPE '\\\\'";
    private static final String SQL_SELECT_CATEGORY_NAME_BY_ID = "SELECT category_name FROM category WHERE category_id = :categoryId";
    private static final String SQL_SELECT_KEYWORD_NAME_BY_ID = "SELECT keyword_name FROM keyword WHERE keyword_id = :keywordId";
    private static final String SQL_SELECT_COUNTRY_NAME_BY_ID = "SELECT country_name FROM country WHERE country_id = :countryId";
    private static final String SQL_INSERT_PRODUCT = "INSERT INTO product (product_name, category_id, keyword_id, country_id, manufacture_date, " +
            "regular_price, discount_rate, sale_price, stock_S, stock_m, stock_l, stock_xl, image_path, description) " +
            "VALUES (:productName, :categoryId, :keywordId, :countryId, :manufactureDate, :regularPrice, :discountRate, :salePrice, :stockS, :stockM, :stockL, :stockXL, :imagePath, :description)";
    private static final String SQL_UPDATE_PRODUCT = "UPDATE product SET category_id = :categoryId, keyword_id = :keywordId, country_id = :countryId, manufacture_date = :manufactureDate, regular_price = :regularPrice, discount_rate = :discountRate, sale_price = :salePrice, stock_s = :stockS, stock_m = :stockM, stock_l = :stockL, stock_xl = :stockXL, image_path = :imagePath, description = :description WHERE product_id = :productId";
    private static final String SQL_DELETE_PURCHASE_HISTORY_BY_PRODUCT_ID = "DELETE FROM purchase_history WHERE product_id = :productId";
    private static final String SQL_DELETE_PRODUCT_BY_ID = "DELETE FROM product WHERE product_id = :productId";
    private static final String SQL_DELETE_CART_BY_PRODUCT_ID = "DELETE FROM cart WHERE product_id = :productId";

    // コンストラクター
    public ProductDaoImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    /** 整列基準に基づく商品情報の取得 */
    @Override
    public List<Product> findBySort(String sort) {
        String SQL = SQL_SELECT_BY_SORT + validateSort(sort);
        return template.query(SQL, rowMapper);
    }

    /** 全体商品情報の取得 */
    @Override
    public List<Product> findByAll() {
        return template.query(SQL_SELECT_ALL, rowMapper);
    }

    /** 全体カテゴリー情報の取得 */
    @Override
    public List<Category> findAllCategory() {
        return template.query(SQL_SELECT_ALL_CATEGORY, categoryRowMapper);
    }

    /** 全体キーワード情報の取得 */
    @Override
    public List<Keyword> findAllKeyword() {
        return template.query(SQL_SELECT_ALL_KEYWORD, keywordRowMapper);
    }

    /** 全体製造国情報の取得 */
    @Override
    public List<Country> findAllCountry() {
        return template.query(SQL_SELECT_ALL_COUNTRY, countryRowMapper);
    }

    /** 推薦商品情報の取得 */
    @Override
    public List<Product> findByRecommends() {
        return template.query(SQL_SELECT_BY_RECOMMENDS, rowMapper);
    }

    /** 商品名から商品IDを取得 */
    @Override
    public Integer findProductIdByProductName(String productName) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("productName", productName);
        List<Integer> result = template.queryForList(SQL_SELECT_PRODUCT_ID_BY_NAME, param, Integer.class);
        return result.isEmpty() ? null : result.get(0);
    }

    /** カテゴリーIDからカテゴリー名を取得 */
    @Override
    public String findCategoryNameByCategoryId(Integer categoryId) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("categoryId", categoryId);
        List<Category> categoryList = template.query(SQL_SELECT_CATEGORY_NAME_BY_ID, param, categoryRowMapper);
        return categoryList.isEmpty() ? null : categoryList.get(0).getCategoryName();
    }

    /** キーワードIDからキーワード名を取得 */
    @Override
    public String findKeywordNameByKeywordId(Integer keywordId) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("keywordId", keywordId);
        List<Keyword> keywordList = template.query(SQL_SELECT_KEYWORD_NAME_BY_ID, param, keywordRowMapper);
        return keywordList.isEmpty() ? null : keywordList.get(0).getKeywordName();
    }

    /** 製造国IDから国名を取得 */
    @Override
    public String findCountryNameByCountryId(Integer countryId) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("countryId", countryId);
        List<Country> countryList = template.query(SQL_SELECT_COUNTRY_NAME_BY_ID, param, countryRowMapper);
        return countryList.isEmpty() ? null : countryList.get(0).getCountryName();
    }

    /** 商品IDによる商品情報の取得 */
    @Override
    public Product findByProductId(Integer productId) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("productId", productId);
        List<Product> result = template.query(SQL_SELECT_BY_PRODUCTID, param, rowMapper);
        return result.isEmpty() ? null : result.get(0);
    }

    /** 商品名による商品検索（整列基準付き） */
    @Override
    public List<Product> findByProductName(String productName, String sort) {
        String SQL = SQL_SELECT_BY_PRODUCTNAME + validateSort(sort);

        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("productName", "%" + escapeLike(productName) + "%");

        return template.query(SQL, param, rowMapper);
    }

    /** キーワード名による商品検索（整列基準付き） */
    @Override
    public List<Product> findByKeywordName(String keywordName, String sort) {
        String SQL = SQL_SELECT_BY_KEYWORDID + validateSort(sort);

        MapSqlParameterSource paramKeyword = new MapSqlParameterSource();
        paramKeyword.addValue("keywordName", "%" + escapeLike(keywordName) + "%");

        List<Keyword> keywordIdList = template.query(SQL_SELECT_BY_KEYWORD, paramKeyword, keywordRowMapper);
        if (keywordIdList.isEmpty()) {
            return template.query(SQL_SELECT_ALL, rowMapper);
        }

        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("keywordId", keywordIdList.get(0).getKeywordId());
        return template.query(SQL, param, rowMapper);
    }

    /** カテゴリーIDによる商品検索（整列基準付き） */
    @Override
    public List<Product> findByCategory(int categoryId, String sort) {
        String SQL = SQL_SELECT_BY_CATEGORY + validateSort(sort);
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("categoryId", categoryId);
        return template.query(SQL, param, rowMapper);
    }

    /** カテゴリーIDおよび商品名による商品検索（整列基準付き） */
    @Override
    public List<Product> findByCategoryAndProductName(int categoryId, String sort, String productName) {
        String SQL = SQL_SELECT_BY_CATEGORY_PRODUCTNAME + validateSort(sort);

        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("categoryId", categoryId);
        param.addValue("productName", "%" + escapeLike(productName) + "%");

        return template.query(SQL, param, rowMapper);
    }

    /** カテゴリーIDおよびキーワード名による商品検索（整列基準付き） */
    @Override
    public List<Product> findByCategoryAndKeyword(int categoryId, String sort, String keywordName) {
        String SQL = SQL_SELECT_BY_CATEGORY_KEYWORDID + validateSort(sort);

        MapSqlParameterSource paramKeyword = new MapSqlParameterSource();
        paramKeyword.addValue("keywordName", "%" + escapeLike(keywordName) + "%");

        List<Keyword> keywordIdList = template.query(SQL_SELECT_BY_KEYWORD, paramKeyword, keywordRowMapper);
        if (keywordIdList.isEmpty()) {
            return template.query(SQL_SELECT_ALL, rowMapper);
        }

        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("categoryId", categoryId);
        param.addValue("keywordId", keywordIdList.get(0).getKeywordId());
        return template.query(SQL, param, rowMapper);
    }

    /** 新規商品登録 */
    @Override
    public void insertProduct(ProductDto dto) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("productName", dto.getProductName())
                .addValue("categoryId", dto.getCategoryId())
                .addValue("keywordId", dto.getKeywordId())
                .addValue("countryId", dto.getCountryId())
                .addValue("manufactureDate", dto.getManufactureDate())
                .addValue("regularPrice", dto.getRegularPrice())
                .addValue("discountRate", dto.getDiscountRate())
                .addValue("salePrice", dto.getSalePrice())
                .addValue("stockS", dto.getStockS())
                .addValue("stockM", dto.getStockM())
                .addValue("stockL", dto.getStockL())
                .addValue("stockXL", dto.getStockXL())
                .addValue("imagePath", dto.getImagePath())
                .addValue("description", dto.getDescription());

        template.update(SQL_INSERT_PRODUCT, params);
    }

    /** 商品情報の更新 */
    @Override
    public void updateProduct(ProductDto productDto) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("productId", productDto.getProductId())
                .addValue("categoryId", productDto.getCategoryId())
                .addValue("keywordId", productDto.getKeywordId())
                .addValue("countryId", productDto.getCountryId())
                .addValue("manufactureDate", productDto.getManufactureDate())
                .addValue("regularPrice", productDto.getRegularPrice())
                .addValue("discountRate", productDto.getDiscountRate())
                .addValue("salePrice", productDto.getSalePrice())
                .addValue("stockS", productDto.getStockS())
                .addValue("stockM", productDto.getStockM())
                .addValue("stockL", productDto.getStockL())
                .addValue("stockXL", productDto.getStockXL())
                .addValue("imagePath", productDto.getImagePath())
                .addValue("description", productDto.getDescription());

        template.update(SQL_UPDATE_PRODUCT, params);
    }

    /** 指定された商品IDの商品情報を削除 */
    @Override
    public void deleteProductById(Integer productId) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("productId", productId);

        template.update(SQL_DELETE_CART_BY_PRODUCT_ID, param);
        template.update(SQL_DELETE_PURCHASE_HISTORY_BY_PRODUCT_ID, param);
        template.update(SQL_DELETE_PRODUCT_BY_ID, param);
    }

    // SQL文設定
    private String escapeLike(String keyword) {
        return keyword.replace("\\", "\\\\").replace("_", "\\_").replace("%", "\\%");
    }

    // 整列県書
    private String validateSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return "created_at";
        }
        List<String> allowedSorts = List.of("created_at", "sale_price", "discount_rate");
        String lowerSort = sort.toLowerCase();

        if (!allowedSorts.contains(lowerSort)) {
            throw new IllegalArgumentException("並び順のエラー: " + sort);
        }
        return lowerSort;
    }
    
}

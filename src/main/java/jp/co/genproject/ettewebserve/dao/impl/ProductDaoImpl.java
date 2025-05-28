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
 * 商品一覧取得、検索、詳細表示、推薦商品の取得など、
 * 商品に関するデータベース操作を実装する。
 *
 * 主な機能：
 * ・商品一覧の取得
 * ・商品名、カテゴリ、キーワードによる検索
 * ・推薦商品の取得
 * ・商品詳細および付随情報（カテゴリ名、キーワード名、国名）の取得
 * ・並び順のバリデーションチェック（SQLインジェクション対策含む）
 *
 * 使用技術：
 * ・Spring JDBC
 * ・NamedParameterJdbcTemplate
 * ・BeanPropertyRowMapper
 *
 * @author 張勝現
 * @version 1.0
 */
@Repository
public class ProductDaoImpl implements ProductDao {

    private final NamedParameterJdbcTemplate template;

    private final BeanPropertyRowMapper<Category> categoryRowMapper = new BeanPropertyRowMapper<>(Category.class);
    private final BeanPropertyRowMapper<Keyword> keywordRowMapper = new BeanPropertyRowMapper<>(Keyword.class);
    private final BeanPropertyRowMapper<Country> countryRowMapper = new BeanPropertyRowMapper<>(Country.class);
    private final BeanPropertyRowMapper<Product> rowMapper = new BeanPropertyRowMapper<>(Product.class);

    // ----------------------------------------
    // ▼ 商品取得・検索関連SQL
    // ----------------------------------------
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

    // ----------------------------------------
    // ▼ マスター情報取得SQL
    // ----------------------------------------
    private static final String SQL_SELECT_ALL_CATEGORY = "SELECT * FROM category ORDER BY category_id";
    private static final String SQL_SELECT_ALL_KEYWORD = "SELECT * FROM keyword ORDER BY keyword_id";
    private static final String SQL_SELECT_ALL_COUNTRY = "SELECT * FROM country ORDER BY country_id";
    private static final String SQL_SELECT_BY_KEYWORD = "SELECT * FROM keyword WHERE keyword_name LIKE :keywordName ESCAPE '\\\\'";
    private static final String SQL_SELECT_CATEGORY_NAME_BY_ID = "SELECT category_name FROM category WHERE category_id = :categoryId";
    private static final String SQL_SELECT_KEYWORD_NAME_BY_ID = "SELECT keyword_name FROM keyword WHERE keyword_id = :keywordId";
    private static final String SQL_SELECT_COUNTRY_NAME_BY_ID = "SELECT country_name FROM country WHERE country_id = :countryId";

    // ----------------------------------------
    // ▼ 商品登録SQL
    // ----------------------------------------
    private static final String SQL_INSERT_PRODUCT = "INSERT INTO product (product_name, category_id, keyword_id, country_id, manufacture_date, "
            +
            "regular_price, discount_rate, sale_price, stock_S, stock_m, stock_l, stock_xl, image_path, description) " +
            "VALUES (:productName, :categoryId, :keywordId, :countryId, :manufactureDate, :regularPrice, :discountRate, :salePrice, :stockS, :stockM, :stockL, :stockXL, :imagePath, :description)";
    private static final String SQL_UPDATE_PRODUCT = "UPDATE product SET category_id = :categoryId, keyword_id = :keywordId, country_id = :countryId, manufacture_date = :manufactureDate, regular_price = :regularPrice, discount_rate = :discountRate, sale_price = :salePrice, stock_s = :stockS, stock_m = :stockM, stock_l = :stockL, stock_xl = :stockXL, image_path = :imagePath, description = :description WHERE product_id = :productId";

    public ProductDaoImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    // 商品一覧取得
    @Override
    public List<Product> findByAll() {
        return template.query(SQL_SELECT_ALL, rowMapper);
    }

    // 商品名でID取得
    @Override
    public Integer findProductIdByProductName(String productName) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("productName", productName);
        List<Integer> result = template.queryForList(SQL_SELECT_PRODUCT_ID_BY_NAME, param, Integer.class);
        return result.isEmpty() ? null : result.get(0);
    }

    // カテゴリー一覧取得
    @Override
    public List<Category> findAllCategory() {
        return template.query(SQL_SELECT_ALL_CATEGORY, categoryRowMapper);
    }

    // キーワード一覧取得
    @Override
    public List<Keyword> findAllKeyword() {
        return template.query(SQL_SELECT_ALL_KEYWORD, keywordRowMapper);
    }

    // 製造国一覧取得
    @Override
    public List<Country> findAllCountry() {
        return template.query(SQL_SELECT_ALL_COUNTRY, countryRowMapper);
    }

    // 並び順のみで商品検索
    @Override
    public List<Product> findBySort(String sort) {
        String SQL = SQL_SELECT_BY_SORT + validateSort(sort);
        return template.query(SQL, rowMapper);
    }

    // 商品名で検索
    public List<Product> findByProductName(String productName, String sort) {
        String SQL = SQL_SELECT_BY_PRODUCTNAME + validateSort(sort);

        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("productName", "%" + escapeLike(productName) + "%");

        return template.query(SQL, param, rowMapper);
    }

    // キーワード名で検索（キーワードIDへ変換）
    @Override
    public List<Product> findByKeywordName(String keywordName, String sort) {
        String SQL = SQL_SELECT_BY_KEYWORDID + validateSort(sort);

        MapSqlParameterSource paramKeyword = new MapSqlParameterSource();
        paramKeyword.addValue("keywordName", "%" + escapeLike(keywordName) + "%"); // 부분일치 검색

        List<Keyword> keywordIdList = template.query(SQL_SELECT_BY_KEYWORD, paramKeyword, keywordRowMapper);
        if (keywordIdList.isEmpty()) {
            return template.query(SQL_SELECT_ALL, rowMapper);
        }

        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("keywordId", keywordIdList.get(0).getKeywordId());
        return template.query(SQL, param, rowMapper);
    }

    // カテゴリーIDで検索
    @Override
    public List<Product> findByCategory(int categoryId, String sort) {
        String SQL = SQL_SELECT_BY_CATEGORY + validateSort(sort);
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("categoryId", categoryId);
        return template.query(SQL, param, rowMapper);
    }

    // カテゴリー + 商品名で検索
    @Override
    public List<Product> findByCategoryAndProductName(int categoryId, String sort, String productName) {
        String SQL = SQL_SELECT_BY_CATEGORY_PRODUCTNAME + validateSort(sort);

        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("categoryId", categoryId);
        param.addValue("productName", "%" + escapeLike(productName) + "%");

        return template.query(SQL, param, rowMapper);
    }

    // カテゴリー + キーワード名で検索（キーワードIDに変換）
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

    // 推薦商品取得
    @Override
    public List<Product> findByRecommends() {
        return template.query(SQL_SELECT_BY_RECOMMENDS, rowMapper);
    }



    // 商品IDで詳細取得
    @Override
    public Product findByProductId(Integer productId) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("productId", productId);
        List<Product> result = template.query(SQL_SELECT_BY_PRODUCTID, param, rowMapper);
        return result.isEmpty() ? null : result.get(0);
    }

    // カテゴリーIDから名前取得
    @Override
    public String findCategoryNameByCategoryId(Integer categoryId) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("categoryId", categoryId);
        List<Category> categoryList = template.query(SQL_SELECT_CATEGORY_NAME_BY_ID, param, categoryRowMapper);
        return categoryList.isEmpty() ? null : categoryList.get(0).getCategoryName();
    }

    // キーワードIDから名前取得
    @Override
    public String findKeywordNameByKeywordId(Integer keywordId) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("keywordId", keywordId);
        List<Keyword> keywordList = template.query(SQL_SELECT_KEYWORD_NAME_BY_ID, param, keywordRowMapper);
        return keywordList.isEmpty() ? null : keywordList.get(0).getKeywordName();
    }

    // 国IDから名前取得
    @Override
    public String findCountryNameByCountryId(Integer countryId) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("countryId", countryId);
        List<Country> countryList = template.query(SQL_SELECT_COUNTRY_NAME_BY_ID, param, countryRowMapper);
        return countryList.isEmpty() ? null : countryList.get(0).getCountryName();
    }

    // 商品登録処理
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

    private String escapeLike(String keyword) {
        return keyword.replace("\\", "\\\\").replace("_", "\\_").replace("%", "\\%");
    }

    // 並び順の値が正当かを検証（SQLインジェクション対策）
    private String validateSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return "created_at";
        }

        List<String> allowedSorts = List.of("created_at", "sale_price", "discount_rate");
        if (!allowedSorts.contains(sort.toLowerCase())) {
            throw new IllegalArgumentException("並び順のエラー: " + sort);
        }

        return sort;
    }
}

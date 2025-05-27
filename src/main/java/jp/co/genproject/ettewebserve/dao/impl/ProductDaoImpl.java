package jp.co.genproject.ettewebserve.dao.impl;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import jp.co.genproject.ettewebserve.dao.ProductDao;
import jp.co.genproject.ettewebserve.entity.Category;
import jp.co.genproject.ettewebserve.entity.Country;
import jp.co.genproject.ettewebserve.entity.Keyword;
import jp.co.genproject.ettewebserve.entity.Product;

@Repository
public class ProductDaoImpl implements ProductDao{
    private final NamedParameterJdbcTemplate template;
    private final BeanPropertyRowMapper<Product> rowMapper = new BeanPropertyRowMapper<Product>(Product.class);
    private final BeanPropertyRowMapper<Keyword> keywordRowMapper = new BeanPropertyRowMapper<Keyword>(Keyword.class);
    private final BeanPropertyRowMapper<Category> categoryRowMapper = new BeanPropertyRowMapper<Category>(Category.class);
    private final BeanPropertyRowMapper<Country> countryRowMapper = new BeanPropertyRowMapper<Country>(Country.class);

    private final String SQL_SELECT_ALL = "SELECT * FROM product ORDER BY product_id";
    private final String SQL_SELECT_BY_SORT = "SELECT * FROM product ORDER BY";
    private final String SQL_SELECT_BY_CATEGORY = "SELECT * FROM product WHERE category_id = :categoryId ORDER BY ";
    private final String SQL_SELECT_BY_PRODUCTNAME = "SELECT * FROM product WHERE product_name = :productName ORDER BY ";
    private final String SQL_SELECT_BY_KEYWORDID = "SELECT * FROM product WHERE keyword_id = :keywordId ORDER BY ";
    private final String SQL_SELECT_BY_CATEGORY_PRODUCTNAME = "SELECT * FROM product WHERE category_id = :categoryId and product_name = :productName ORDER BY ";
    private final String SQL_SELECT_BY_CATEGORY_KEYWORDID = "SELECT * FROM product WHERE category_id = :categoryId and keyword_id = :keywordId ORDER BY ";
    private final String SQL_SELECT_BY_KEYWORD = "SELECT * FROM keyword WHERE keyword_name = :keywordName";
    private final String SQL_SELECT_BY_RECOMMENDS = "SELECT p.product_id, p.product_name, p.regular_price, p.sale_price, p.imagePath FROM recommends r JOIN product p ON r.product_id = p.product_id";
    private final String SQL_SELECT_BY_PRODUCTID = "SELECT * FROM product WHERE product_id = :productId ORDER BY product_id";
    private final String SQL_SELECT_CATEGORY_NAME_BY_ID = "SELECT category_name FROM category WHERE category_id = :categoryId";
    private final String SQL_SELECT_KEYWORD_NAME_BY_ID = "SELECT keyword_name FROM keyword WHERE keyword_id = :keywordId";
    private final String SQL_SELECT_COUNTRY_NAME_BY_ID = "SELECT country_name FROM country WHERE country_id = :countryId";
    
    public ProductDaoImpl(NamedParameterJdbcTemplate template){
        this.template = template;
    }
    
    //　「全体」検索
    public List<Product> findByAll(){
        return template.query(SQL_SELECT_ALL, rowMapper);
    }

    // 「並び順」で検索
    public List<Product> findBySort(String sort){
        String SQL = SQL_SELECT_BY_SORT + validateSort(sort);
        return template.query(SQL, rowMapper);
    }

    // 「商品名 + 並び順」で検索
    public List<Product> findByProductName(String productName, String sort){
        // SQL文の並び順を適用
        String SQL = SQL_SELECT_BY_PRODUCTNAME + validateSort(sort);

        // SQL文のパラメータインスタンス変数を指定
        MapSqlParameterSource param = new MapSqlParameterSource();

        // SQL文の商品名を適用
        param.addValue("productName", productName);
        return template.query(SQL, param, rowMapper);
    }

    // 「キーワード + 並び順」で検索
    public List<Product> findByKeywordName(String keywordName, String sort){
        // SQL文の並び順を適用
        String SQL = SQL_SELECT_BY_KEYWORDID + validateSort(sort);
        
        // キーワードで、キーワードIDの探す
        MapSqlParameterSource paramKeyword = new MapSqlParameterSource();
        paramKeyword.addValue("keywordName", keywordName);
        List<Keyword> keywordIdList = template.query(SQL_SELECT_BY_KEYWORD, paramKeyword, keywordRowMapper);
        if (keywordIdList.isEmpty()) {
            return template.query(SQL_SELECT_ALL, rowMapper); // keywordがないと全体リスト出力
        }
        Keyword keywordIdProduct = keywordIdList.get(0);
        int keywordId = keywordIdProduct.getKeywordId();

        // SQL文のキーワードIDを適用
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("keywordId", keywordId);
        return template.query(SQL, param, rowMapper);
    }

    // 「カテゴリー + 並び順」で検索
    public List<Product> findByCategory(int categoryId, String sort){
        // SQL文の並び順を適用
        String SQL = SQL_SELECT_BY_CATEGORY + validateSort(sort);

        // SQL文のパラメータインスタンス変数を指定
        MapSqlParameterSource param = new MapSqlParameterSource();

        // SQL文のカテゴリーIDと商品名を適用
        param.addValue("categoryId", categoryId);
        return template.query(SQL, param, rowMapper);
    }
    
    // 「カテゴリー + 商品名 + 並び順」で検索
    public List<Product> findByCategoryAndProductName(int categoryId, String sort, String productName){
        // SQL文の並び順を適用
        String SQL = SQL_SELECT_BY_CATEGORY_PRODUCTNAME + validateSort(sort);
        
        // SQL文のパラメータインスタンス変数を指定
        MapSqlParameterSource param = new MapSqlParameterSource();

        // SQL文のカテゴリーIDと商品名を適用
        param.addValue("categoryId", categoryId);
        param.addValue("productName", productName);
        return template.query(SQL, param, rowMapper);
    }

    // 「カテゴリー + キーワード + 並び順」で検索
    public List<Product> findByCategoryAndKeyword(int categoryId, String sort, String keywordName){
        // SQL文の並び順を適用
        String SQL = SQL_SELECT_BY_CATEGORY_KEYWORDID + validateSort(sort);
        
        // キーワードで、キーワードIDの探す
        MapSqlParameterSource paramKeyword = new MapSqlParameterSource();
        paramKeyword.addValue("keywordName", keywordName);
        List<Keyword> keywordIdList = template.query(SQL_SELECT_BY_KEYWORD, paramKeyword, keywordRowMapper);
        if (keywordIdList.isEmpty()) {
            return template.query(SQL_SELECT_ALL, rowMapper); // keywordがないと全体リスト出力
        }
        Keyword keywordIdProduct = keywordIdList.get(0);
        int keywordId = keywordIdProduct.getKeywordId();

        // SQL文のカテゴリーIDとキーワードIDを適用
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("categoryId", categoryId);
        param.addValue("keywordId", keywordId);
        return template.query(SQL, param, rowMapper);
    }

    // 推薦商品リスト
    public List<Product> findByRecommends(){
        return template.query(SQL_SELECT_BY_RECOMMENDS, rowMapper);

    }

    // SQLインジェクション防止
    private String validateSort(String sort) {
        List<String> allowedSorts = List.of("created_at", "sale_price", "discount_rate");
        if (!allowedSorts.contains(sort.toLowerCase())) {
            throw new IllegalArgumentException("並び順のエラー" + sort);
        }
        return sort;
    }

    // 「商品ID」で探す
    public Product findByProductId(Integer productId){
        // SQL文のパラメータインスタンス変数を指定
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("productId", productId);

        List<Product> result = template.query(SQL_SELECT_BY_PRODUCTID, param, rowMapper);

        // index[0]を返却
        return result.isEmpty() ? null : result.get(0);
    }

    // 「カテゴリーID」で、カテゴリー名探す
    public String findCategoryNameByCategoryId(Integer categoryId) {
        // SQL文のパラメータインスタンス変数を指定
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("categoryId", categoryId);

        List<Category> categoryList = template.query(SQL_SELECT_CATEGORY_NAME_BY_ID, param, categoryRowMapper);

        if (categoryList.isEmpty()) {
            return null;
        }

        // index[0]のフィールドを返却
        return categoryList.get(0).getCategoryName();
    }

    // 「キーワードID」で、キーワード名探す
    public String findKeywordNameByKeywordId(Integer keywordId) {
        // SQL文のパラメータインスタンス変数を指定
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("keywordId", keywordId);

        List<Keyword> keywordList = template.query(SQL_SELECT_KEYWORD_NAME_BY_ID, param, keywordRowMapper);

        if (keywordList.isEmpty()) {
            return null;
        }

        // index[0]のフィールドを返却
        return keywordList.get(0).getKeywordName();
    }

    public String findCountryNameByCountryId(Integer countryId){
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("countryId", countryId);

        List<Country> countryList = template.query(SQL_SELECT_COUNTRY_NAME_BY_ID, param, countryRowMapper);

        return countryList.get(0).getCountryName();
    }
}

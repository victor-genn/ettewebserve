package jp.co.genproject.ettewebserve.dao;

import java.util.List;

import jp.co.genproject.ettewebserve.dto.ProductDto;
import jp.co.genproject.ettewebserve.entity.Category;
import jp.co.genproject.ettewebserve.entity.Country;
import jp.co.genproject.ettewebserve.entity.Keyword;
import jp.co.genproject.ettewebserve.entity.Product;

public interface ProductDao {

    /** 整列基準に基づく商品情報の取得 */
    List<Product> findBySort(String sort);

    /** 全体商品情報の取得 */
    List<Product> findByAll();

    /** 全体カテゴリー情報の取得 */
    List<Category> findAllCategory();

    /** 全体キーワード情報の取得 */
    List<Keyword> findAllKeyword();

    /** 全体製造国情報の取得 */
    List<Country> findAllCountry();

    /** 推薦商品情報の取得 */
    List<Product> findByRecommends();

    /** 商品名から商品IDを取得 */
    Integer findProductIdByProductName(String productName);

    /** カテゴリーIDからカテゴリー名を取得 */
    String findCategoryNameByCategoryId(Integer categoryId);

    /** キーワードIDからキーワード名を取得 */
    String findKeywordNameByKeywordId(Integer keywordId);

    /** 製造国IDから国名を取得 */
    String findCountryNameByCountryId(Integer countryId);

    /** 商品IDによる商品情報の取得 */
    Product findByProductId(Integer productId);

    /** 商品名による商品検索（整列基準付き） */
    List<Product> findByProductName(String productName, String sort);

    /** キーワード名による商品検索（整列基準付き） */
    List<Product> findByKeywordName(String keywordName, String sort);

    /** カテゴリーIDによる商品検索（整列基準付き） */
    List<Product> findByCategory(int categoryId, String sort);

    /** カテゴリーIDおよび商品名による商品検索（整列基準付き） */
    List<Product> findByCategoryAndProductName(int categoryId, String sort, String productName);

    /** カテゴリーIDおよびキーワード名による商品検索（整列基準付き） */
    List<Product> findByCategoryAndKeyword(int categoryId, String sort, String keywordName);

    /** 新規商品登録 */
    void insertProduct(ProductDto dto);

    /** 商品情報の更新 */
    void updateProduct(ProductDto productDto);

    /** 商品IDによる削除処理 */
    void deleteProductById(Integer productId);
}
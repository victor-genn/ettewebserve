package jp.co.genproject.ettewebserve.dao;

import java.util.List;

import jp.co.genproject.ettewebserve.dto.ProductDto;
import jp.co.genproject.ettewebserve.entity.Category;
import jp.co.genproject.ettewebserve.entity.Country;
import jp.co.genproject.ettewebserve.entity.Keyword;
import jp.co.genproject.ettewebserve.entity.Product;

/**
 * 商品関連機能のDAOインターフェースクラス。
 * 商品一覧表示、検索、詳細表示などの商品に関する
 * データアクセス処理を定義する。
 *
 * 主な機能：
 * ・商品一覧取得
 * ・商品検索（名前・キーワード・カテゴリ）
 * ・推薦商品取得
 * ・商品詳細情報の取得
 *
 * 使用技術：
 * ・Spring JDBC
 *
 * @author 張勝現
 * @version 1.0
 */
public interface ProductDao {

    // 商品検索用
    // 「全体」検索
    public List<Product> findByAll();

    public Integer findProductIdByProductName(String productName);

    public List<Category> findAllCategory();

    public List<Keyword> findAllKeyword();

    public List<Country> findAllCountry();

    // 「並び順」で検索
    public List<Product> findBySort(String sort);

    // 「商品名 + 並び順」で検索
    public List<Product> findByProductName(String productName, String sort);

    // 「キーワード + 並び順」で検索
    public List<Product> findByKeywordName(String keywordName, String sort);

    // 「カテゴリー + 並び順」で検索
    public List<Product> findByCategory(int categoryId, String sort);

    // 「カテゴリー + 商品名 + 並び順」で検索
    public List<Product> findByCategoryAndProductName(int categoryId, String sort, String productName);

    // 「カテゴリー + キーワード + 並び順」で検索
    public List<Product> findByCategoryAndKeyword(int categoryId, String sort, String keywordName);

    // 推薦商品リスト
    public List<Product> findByRecommends();

    // 商品詳細用
    // 「商品ID」で探す
    public Product findByProductId(Integer productId);

    // 「カテゴリーID」で、カテゴリー名探す
    public String findCategoryNameByCategoryId(Integer categoryId);

    // 「キーワードID」で、キーワード名探す
    public String findKeywordNameByKeywordId(Integer keywordId);

    // 「製造国ID」で、製造国名探す
    public String findCountryNameByCountryId(Integer countryId);

    // 新規商品登録
    public void insertProduct(ProductDto productDto);

    // 商品情報更新
    public void updateProduct(ProductDto productDto);
}

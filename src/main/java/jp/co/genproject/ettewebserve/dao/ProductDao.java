package jp.co.genproject.ettewebserve.dao;

import java.util.List;

import jp.co.genproject.ettewebserve.entity.Product;

public interface ProductDao {

    // 商品検索用    
    //　「全体」検索
    public List<Product> findByAll();

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
}

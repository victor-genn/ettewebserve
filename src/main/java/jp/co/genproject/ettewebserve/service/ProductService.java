package jp.co.genproject.ettewebserve.service;

import java.util.List;

import jp.co.genproject.ettewebserve.dto.ProductDto;
import jp.co.genproject.ettewebserve.entity.Category;
import jp.co.genproject.ettewebserve.entity.Country;
import jp.co.genproject.ettewebserve.entity.Keyword;
import jp.co.genproject.ettewebserve.entity.Product;

public interface ProductService {

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

    public void updateProduct(ProductDto productDto);
}

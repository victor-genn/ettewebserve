package jp.co.genproject.ettewebserve.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import jp.co.genproject.ettewebserve.dao.ProductDao;
import jp.co.genproject.ettewebserve.dto.ProductDto;
import jp.co.genproject.ettewebserve.entity.Category;
import jp.co.genproject.ettewebserve.entity.Country;
import jp.co.genproject.ettewebserve.entity.Keyword;
import jp.co.genproject.ettewebserve.entity.Product;
import jp.co.genproject.ettewebserve.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductDao productDao;

    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    // 「全体」検索
    public List<Product> findByAll() {
        return productDao.findByAll();
    }

    public Integer findProductIdByProductName(String productName){
        return productDao.findProductIdByProductName(productName);
    }

    public List<Category> findAllCategory(){
        return productDao.findAllCategory();
    }

    public List<Keyword> findAllKeyword(){
        return productDao.findAllKeyword();
    }

    public List<Country> findAllCountry(){
        return productDao.findAllCountry();
    }

    // 「並び順」で検索
    public List<Product> findBySort(String sort) {
        return productDao.findBySort(sort);
    }

    // 「商品名 + 並び順」で検索
    public List<Product> findByProductName(String productName, String sort) {
        return productDao.findByProductName(productName, sort);
    }

    // 「キーワード + 並び順」で検索
    public List<Product> findByKeywordName(String keywordName, String sort) {
        return productDao.findByKeywordName(keywordName, sort);
    }

    // 「カテゴリー + 並び順」で検索
    public List<Product> findByCategory(int categoryId, String sort) {
        return productDao.findByCategory(categoryId, sort);
    }

    // 「カテゴリー + 商品名 + 並び順」で検索
    public List<Product> findByCategoryAndProductName(int categoryId, String sort, String productName) {
        return productDao.findByCategoryAndProductName(categoryId, sort, productName);
    }

    // 「カテゴリー + キーワード + 並び順」で検索
    public List<Product> findByCategoryAndKeyword(int categoryId, String sort, String keywordName) {
        return productDao.findByCategoryAndProductName(categoryId, sort, keywordName);
    }

    // 推薦商品リスト
    public List<Product> findByRecommends() {
        return productDao.findByRecommends();
    }

    // 「商品ID」で探す
    public Product findByProductId(Integer productId) {
        return productDao.findByProductId(productId);
    }

    // 「カテゴリーID」で、カテゴリー名探す
    public String findCategoryNameByCategoryId(Integer categoryId) {
        return productDao.findCategoryNameByCategoryId(categoryId);
    }

    // 「キーワードID」で、キーワード名探す
    public String findKeywordNameByKeywordId(Integer keywordId) {
        return productDao.findCategoryNameByCategoryId(keywordId);
    }

    // 「製造国ID」で、製造国名探す
    public String findCountryNameByCountryId(Integer countryId) {
        return productDao.findCountryNameByCountryId(countryId);
    }

    // 新規商品登録
    public void insertProduct(ProductDto productDto) {
        productDao.insertProduct(productDto);
    }

    public void updateProduct(ProductDto productDto){
        productDao.updateProduct(productDto);
    }
}

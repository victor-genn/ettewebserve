package jp.co.genproject.ettewebserve.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.genproject.ettewebserve.dao.ProductDao;
import jp.co.genproject.ettewebserve.dto.ProductDto;
import jp.co.genproject.ettewebserve.entity.Category;
import jp.co.genproject.ettewebserve.entity.Country;
import jp.co.genproject.ettewebserve.entity.Keyword;
import jp.co.genproject.ettewebserve.entity.Product;
import jp.co.genproject.ettewebserve.service.ProductService;

/**
 * 商品関連サービスの実装クラス。
 * DAO層を通じて商品情報の取得・検索・更新・登録などの処理を提供する。
 *
 * @author 張勝現
 * @version 1.0
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;

    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<Product> findBySort(String sort) {
        return productDao.findBySort(sort);
    }

    @Override
    public List<Product> findByAll() {
        return productDao.findByAll();
    }

    @Override
    public List<Category> findAllCategory() {
        return productDao.findAllCategory();
    }

    @Override
    public List<Keyword> findAllKeyword() {
        return productDao.findAllKeyword();
    }

    @Override
    public List<Country> findAllCountry() {
        return productDao.findAllCountry();
    }

    @Override
    public List<Product> findByRecommends() {
        return productDao.findByRecommends();
    }

    @Override
    public Integer findProductIdByProductName(String productName) {
        return productDao.findProductIdByProductName(productName);
    }

    @Override
    public String findCategoryNameByCategoryId(Integer categoryId) {
        return productDao.findCategoryNameByCategoryId(categoryId);
    }

    @Override
    public String findKeywordNameByKeywordId(Integer keywordId) {
        return productDao.findKeywordNameByKeywordId(keywordId);
    }

    @Override
    public String findCountryNameByCountryId(Integer countryId) {
        return productDao.findCountryNameByCountryId(countryId);
    }

    @Override
    public Product findByProductId(Integer productId) {
        return productDao.findByProductId(productId);
    }

    @Override
    public List<Product> findByProductName(String productName, String sort) {
        return productDao.findByProductName(productName, sort);
    }

    @Override
    public List<Product> findByKeywordName(String keywordName, String sort) {
        return productDao.findByKeywordName(keywordName, sort);
    }

    @Override
    public List<Product> findByCategory(int categoryId, String sort) {
        return productDao.findByCategory(categoryId, sort);
    }

    @Override
    public List<Product> findByCategoryAndProductName(int categoryId, String sort, String productName) {
        return productDao.findByCategoryAndProductName(categoryId, sort, productName);
    }

    @Override
    public List<Product> findByCategoryAndKeyword(int categoryId, String sort, String keywordName) {
        return productDao.findByCategoryAndKeyword(categoryId, sort, keywordName);
    }

    @Override
    public void insertProduct(ProductDto dto) {
        productDao.insertProduct(dto);
    }

    @Override
    public void updateProduct(ProductDto productDto) {
        productDao.updateProduct(productDto);
    }

    @Override
    public void deleteProductById(Integer productId){
        productDao.deleteProductById(productId);
    }

    @Transactional
    @Override
    public void deleteProductWithCart(int productId) {
        productDao.deleteProductById(productId);
    }
}
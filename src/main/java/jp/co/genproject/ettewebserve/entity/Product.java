package jp.co.genproject.ettewebserve.entity;

public class Product {
    private Integer productId;
    private String productName;
    private Integer categoryId;
    private Integer keywordId;
    private Integer countryId;
    private String manufactureDate;
    private Integer regularPrice;
    private Integer discountRate;
    private Integer salePrice;
    private Integer stock;
    private String imagePath;
    private String description;
    private String createdAt;
    private String updateAt;

    public Product() {
    }

    public Product(Integer productId, String productName, Integer categoryId, Integer keywordId,
                   Integer countryId, String manufactureDate, Integer regularPrice, Integer discountRate,
                   Integer salePrice, Integer stock, String imagePath, String createdAt, String updateAt) {
        this.productId = productId;
        this.productName = productName;
        this.categoryId = categoryId;
        this.keywordId = keywordId;
        this.countryId = countryId;
        this.manufactureDate = manufactureDate;
        this.regularPrice = regularPrice;
        this.discountRate = discountRate;
        this.salePrice = salePrice;
        this.stock = stock;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(Integer keywordId) {
        this.keywordId = keywordId;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public Integer getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(Integer regularPrice) {
        this.regularPrice = regularPrice;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Integer discountRate) {
        this.discountRate = discountRate;
    }

    public Integer getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}


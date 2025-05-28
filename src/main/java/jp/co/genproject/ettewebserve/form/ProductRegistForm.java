package jp.co.genproject.ettewebserve.form;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Validated
public class ProductRegistForm {

    @NotEmpty(message = "商品名は必須です。")
    private String productName;
    
    @NotNull(message = "カテゴリーは必須です。")
    private Integer categoryId;

    @NotNull(message = "キーワードは必須です。")
    private Integer keywordId;

    @NotNull(message = "製造国は必須です。")
    private Integer countryId;

    @NotEmpty(message = "製造年月は必須です。")
    private String manufactureDate;

    @NotNull(message = "定価は必須です。")
    @Min(value = 0, message = "定価は0以上で入力してください。")
    private Integer regularPrice;

    @NotNull(message = "割引率は必須です。")
    @Min(value = 0, message = "割引率は0〜100の範囲で入力してください。")
    @Max(value = 100, message = "割引率は100以下で入力してください。")
    private Integer discountRate;

    private Integer salePrice;

    @NotNull(message = "Sサイズの在庫を入力してください。")
    @Min(value = 0, message = "在庫は0以上で入力してください。")
    private Integer stockS;

    @NotNull(message = "Mサイズの在庫を入力してください。")
    @Min(value = 0, message = "在庫は0以上で入力してください。")
    private Integer stockM;

    @NotNull(message = "Lサイズの在庫を入力してください。")
    @Min(value = 0, message = "在庫は0以上で入力してください。")
    private Integer stockL;

    @NotNull(message = "XLサイズの在庫を入力してください。")
    @Min(value = 0, message = "在庫は0以上で入力してください。")
    private Integer stockXL;

    private String imagePath;

    @Size(max = 1000, message = "説明は1000文字以内で入力してください。")
    private String description;

    private MultipartFile productImage;

    public MultipartFile getProductImage() {
        return productImage;
    }

    public void setProductImage(MultipartFile productImage) {
        this.productImage = productImage;
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

    public Integer getStockS() {
        return stockS;
    }

    public void setStockS(Integer stockS) {
        this.stockS = stockS;
    }

    public Integer getStockM() {
        return stockM;
    }

    public void setStockM(Integer stockM) {
        this.stockM = stockM;
    }

    public Integer getStockL() {
        return stockL;
    }

    public void setStockL(Integer stockL) {
        this.stockL = stockL;
    }

    public Integer getStockXL() {
        return stockXL;
    }

    public void setStockXL(Integer stockXL) {
        this.stockXL = stockXL;
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

}

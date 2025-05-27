package jp.co.genproject.ettewebserve.form;

public class RecomendsForm {
    private String productName;
    private Integer regularPrice;
    private Integer salePrice;
    private String imagePath;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(Integer regularPrice) {
        this.regularPrice = regularPrice;
    }

    public Integer getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}

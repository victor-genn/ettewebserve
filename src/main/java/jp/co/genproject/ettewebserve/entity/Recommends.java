package jp.co.genproject.ettewebserve.entity;

public class Recommends {
    private Integer recommendId;
    private Integer productId;

    public Recommends(){}

    public Recommends(Integer recommendId, Integer productId){
        this.recommendId = recommendId;
        this.productId = productId;
    }

    public Integer getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(Integer recommendId) {
        this.recommendId = recommendId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}

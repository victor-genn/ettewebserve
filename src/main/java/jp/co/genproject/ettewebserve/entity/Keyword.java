package jp.co.genproject.ettewebserve.entity;

public class Keyword {
    private Integer keywordId;
    private String keywordName;

    public Keyword() {
    }

    public Keyword(Integer keywordId, String keywordName) {
        this.keywordId = keywordId;
        this.keywordName = keywordName;
    }

    public Integer getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(Integer keywordId) {
        this.keywordId = keywordId;
    }

    public String getKeywordName() {
        return keywordName;
    }

    public void setKeywordName(String keywordName) {
        this.keywordName = keywordName;
    }
}

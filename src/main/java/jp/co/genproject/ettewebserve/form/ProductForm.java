package jp.co.genproject.ettewebserve.form;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;

@Validated
public class ProductForm {
    private Integer categoryId;

    @NotEmpty(message = "検索タイプを選択してください。")
    private String searchType;

    private String searchWord;

    @NotEmpty(message = "並び順を選択してください。")
    private String sortOrder;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getSearchType() {
        return searchType;
    }
    
    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}


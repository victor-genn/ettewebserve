package jp.co.genproject.ettewebserve.dto;

public class UserUpdateDto {
    private Integer userId;
    private String password;
    private String email;
    private Integer keyword1;
    private Integer keyword2;
    private Integer keyword3;
    private String imagePath;

    public UserUpdateDto() {
    }

    public UserUpdateDto(Integer userId, String password, String email, Integer keyword1, Integer keyword2,
            Integer keyword3, String imagePath) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.keyword3 = keyword3;
        this.imagePath = imagePath;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getKeyword1() {
        return keyword1;
    }

    public void setKeyword1(Integer keyword1) {
        this.keyword1 = keyword1;
    }

    public Integer getKeyword2() {
        return keyword2;
    }

    public void setKeyword2(Integer keyword2) {
        this.keyword2 = keyword2;
    }

    public Integer getKeyword3() {
        return keyword3;
    }

    public void setKeyword3(Integer keyword3) {
        this.keyword3 = keyword3;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String userImage) {
        this.imagePath = userImage;
    }

}

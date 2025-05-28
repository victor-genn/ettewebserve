package jp.co.genproject.ettewebserve.form;

import org.springframework.web.multipart.MultipartFile;

public class UserUpdateForm {
    private Integer userId;
    private String password;
    private String newPassword;
    private String newPassCheck;
    private String email;
    private String domain;
    private String imagePath;
    private Integer keyword1;
    private Integer keyword2;
    private Integer keyword3;
    private MultipartFile userImage;

    public MultipartFile getUserImage() {
        return userImage;
    }

    public void setUserImage(MultipartFile userImage) {
        this.userImage = userImage;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getNewPassCheck() {
        return newPassCheck;
    }

    public void setNewPassCheck(String newPassCheck) {
        this.newPassCheck = newPassCheck;
    }

    public String getNewPassword() {
        return newPassword;
    }
    
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

}
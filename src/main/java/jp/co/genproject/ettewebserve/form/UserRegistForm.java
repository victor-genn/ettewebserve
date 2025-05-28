package jp.co.genproject.ettewebserve.form;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Validated
public class UserRegistForm {

    @NotEmpty(message = "ログインIDを入力してください。")
    private String loginId;

    @NotEmpty(message = "パスワードを入力してください。")
    @Size(min = 6, max = 20, message = "パスワードは6文字以上20文字以下で入力してください。")
    private String password;

    @NotEmpty(message = "パスワード確認を入力してください。")
    private String passCheck;

    @NotEmpty(message = "姓を入力してください。")
    private String lastName;

    @NotEmpty(message = "名を入力してください。")
    private String firstName;

    @NotEmpty(message = "性別を選択してください。")
    private String gender;

    private String email;

    private String domain;

    private String imagePath;

    @NotNull(message = "キーワード1を選択してください。")
    private Integer keyword1;

    @NotNull(message = "キーワード2を選択してください。")
    private Integer keyword2;

    @NotNull(message = "キーワード3を選択してください。")
    private Integer keyword3;

    private MultipartFile userImage;

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }

    public String getPassCheck() {
        return passCheck;
    }
    
    public void setPassCheck(String passCheck) {
        this.passCheck = passCheck;
    }

    public MultipartFile getUserImage() {
        return userImage;
    }

    public void setUserImage(MultipartFile userImage) {
        this.userImage = userImage;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

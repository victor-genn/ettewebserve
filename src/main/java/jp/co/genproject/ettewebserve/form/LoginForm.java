package jp.co.genproject.ettewebserve.form;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;

@Validated
public class LoginForm {

    @NotEmpty(message = "ログインIDを入力してください。")
    private String loginId;
    @NotEmpty(message = "パスワードを入力してください。")
    private String password;

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
}

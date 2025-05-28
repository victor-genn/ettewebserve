package jp.co.genproject.ettewebserve.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import jp.co.genproject.ettewebserve.entity.User;
import jp.co.genproject.ettewebserve.form.LoginForm;
import jp.co.genproject.ettewebserve.service.UserService;

/**
 * ログイン画面の表示を担当するコントローラークラス。
 *
 * 主な機能：
 * ログイン画面の初期表示を行う。
 *
 * 使用技術：
 * Spring MVC を使用。
 *
 * @author 張勝現
 * @version 1.0
 */
@Controller
public class LoginController {
    private final UserService userService;
    private final HttpSession session;

    public LoginController(UserService userService, HttpSession session) {
        this.userService = userService;
        this.session = session;
    }

    @GetMapping("/login")
    public String showLoginForm(@ModelAttribute("loginForm") LoginForm loginForm, Model model) {

        return "login";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("loginForm") LoginForm loginForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "login";
        }

        String loginId = loginForm.getLoginId();
        String password = loginForm.getPassword();

        boolean isAuthenticated = userService.signIn(loginId, password);

        if (!isAuthenticated) {
            model.addAttribute("loginError", "IDまたはパスワードが正しくありません。");
            return "login";
        }

        User user = userService.findByloginId(loginId);
        Integer userId = user.getUserId();
        Integer roleId = user.getRoleId();
        String userName = user.getFirstName();
        String userImage = user.getImagePath();

        session.setAttribute("userId", userId);
        session.setAttribute("loginId", loginId);
        session.setAttribute("roleId", roleId);
        session.setAttribute("userName", userName);
        session.setAttribute("userImage", userImage);

        System.out.println("=== 로그인 성공 ===");
        System.out.println("userId: " + userId);
        System.out.println("loginId: " + loginId);

        return "redirect:/index";
    }

    @GetMapping("/logOut")
    public String logOut() {
        session.invalidate();
        return "redirect:/index";
    }

}

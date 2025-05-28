package jp.co.genproject.ettewebserve.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import jp.co.genproject.ettewebserve.dto.UserUpdateDto;
import jp.co.genproject.ettewebserve.entity.User;
import jp.co.genproject.ettewebserve.form.UserUpdateForm;
import jp.co.genproject.ettewebserve.service.ProductService;
import jp.co.genproject.ettewebserve.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * ユーザー関連機能のコントローラークラス。
 * 
 * <p>主な機能：</p>
 * <ul>
 *   <li>マイページの表示</li>
 *   <li>ユーザー情報の更新フォーム表示</li>
 *   <li>ユーザー情報の更新処理</li>
 * </ul>
 * 
 * <p>使用技術：Spring MVC / Thymeleaf / MultipartFile</p>
 * 
 * @author 張勝現
 */
@Controller
public class UserController {

    private final UserService userService;
    private final ProductService productService;
    private final HttpSession session;

    public UserController(UserService userService, ProductService productService, HttpSession session){
        this.userService = userService;
        this.productService = productService;
        this.session = session;
    }

    /**
     * マイページを表示します。
     * 
     * @return ユーザーマイページのテンプレートパス
     */
    @GetMapping("/mypage")
    public String mypage() {
        return "userView/userPage";
    }

    /**
     * ユーザー情報の更新画面を表示します。
     *
     * @param userUpdateForm ユーザー更新フォーム
     * @param model モデルオブジェクト
     * @return ユーザー情報更新画面のテンプレートパス
     */
    @GetMapping("/userUpdate")
    public String userUpdate(@ModelAttribute("userUpdateForm") UserUpdateForm userUpdateForm , Model model) {
        Integer userId = getSessionInt(session,"userId");
        String loginId = (String) session.getAttribute("loginId");

        if(userId == null){
            return "index";
        }

        model.addAttribute("categoryList", productService.findAllCategory());
        model.addAttribute("keywordList", productService.findAllKeyword());

        User user = userService.findByloginId(loginId);

        String email = user.getEmail();
        if (email != null && email.contains("@")) {
            String[] emailParts = email.split("@", 2);
            userUpdateForm.setEmail(emailParts[0]);
            userUpdateForm.setDomain("@" + emailParts[1]);
        }

        userUpdateForm.setUserId(user.getUserId());
        userUpdateForm.setKeyword1(user.getKeyword1());
        userUpdateForm.setKeyword2(user.getKeyword2());
        userUpdateForm.setKeyword3(user.getKeyword3());
        userUpdateForm.setImagePath(user.getImagePath());

        model.addAttribute("userUpdateForm", userUpdateForm);
        model.addAttribute("user", user);

        return "userView/userUpdate";
    }

    /**
     * ユーザー情報の更新処理を行います。
     *
     * @param userUpdateForm ユーザー更新フォーム
     * @param bindingResult バリデーション結果
     * @param model モデルオブジェクト
     * @return 更新後の画面遷移パス（成功時はマイページへリダイレクト）
     */
    @PostMapping("/updateUser")
    public String updateUser(@Validated @ModelAttribute("userUpdateForm") UserUpdateForm userUpdateForm, BindingResult bindingResult, Model model) {
        Integer userId = getSessionInt(session, "userId");
        String loginId = (String) session.getAttribute("loginId");

        if (userId == null || loginId == null) {
            return "index";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("titleError", "入力内容に誤りがあります。");
            return "userView/userUpdate";
        }

        String originPass = userUpdateForm.getPassword();
        if (!userService.signIn(loginId, originPass)) {
            model.addAttribute("titleError", "IDまたはパスワードが正しくありません");
            return "userView/userUpdate";
        }

        String newPass = userUpdateForm.getNewPassword();
        String newPassCheck = userUpdateForm.getNewPassCheck();
        String finalPassword;

        if (newPass.isEmpty() && newPassCheck.isEmpty()) {
            finalPassword = originPass;
        } else if (!newPass.equals(newPassCheck)) {
            model.addAttribute("titleError", "パスワードが一致しません");
            return "userView/userUpdate";
        } else {
            finalPassword = newPass;
        }

        String email = userUpdateForm.getEmail() + userUpdateForm.getDomain();

        Integer keyword1 = userUpdateForm.getKeyword1();
        Integer keyword2 = userUpdateForm.getKeyword2();
        Integer keyword3 = userUpdateForm.getKeyword3();

        MultipartFile image = userUpdateForm.getUserImage();
        String imagePath = saveImage(image);

        UserUpdateDto userUpdateDto = new UserUpdateDto(userId, finalPassword, email, keyword1, keyword2, keyword3, imagePath);
        userService.updateUser(userUpdateDto);

        return "redirect:/mypage";
    }

    /**
     * アップロードされた画像ファイルを保存し、保存先パスを返却します。
     *
     * @param image MultipartFile形式の画像ファイル
     * @return 保存された画像の相対パス（保存に失敗した場合はnull）
     */
    private String saveImage(MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            try {
                String uploadDir = "src/main/resources/static/images/userImages/";
                String fileName = image.getOriginalFilename();
                Path path = Paths.get(uploadDir + fileName);
                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                return "/images/userImages/" + fileName;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * セッションから指定されたキーの値を取得し、Integerとして返却します。
     * 
     * @param session HttpSessionオブジェクト
     * @param key セッション属性のキー
     * @return Integer型の値（変換できない場合や存在しない場合はnull）
     */
    private Integer getSessionInt(HttpSession session, String key) {
        Object value = session.getAttribute(key);
        if (value == null) return null;
        try {
            return Integer.valueOf(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}


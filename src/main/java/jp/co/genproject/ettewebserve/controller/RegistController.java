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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import jp.co.genproject.ettewebserve.dto.UserDto;
import jp.co.genproject.ettewebserve.form.UserRegistForm;
import jp.co.genproject.ettewebserve.service.ProductService;
import jp.co.genproject.ettewebserve.service.UserService;

@Controller
public class RegistController {

    private final UserService userService;
    private final ProductService productService;

    public RegistController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/regist")
    public String registView(Model model) {
        model.addAttribute("categoryList", productService.findAllCategory());
        model.addAttribute("keywordList", productService.findAllKeyword());
        return "regist";
    }

    @PostMapping("/userRegist")
    public String postMethodName(@Validated @ModelAttribute("userRegistForm") UserRegistForm userRegist,
            BindingResult result, Model model) {

        model.addAttribute("categoryList", productService.findAllCategory());
        model.addAttribute("keywordList", productService.findAllKeyword());
        
        if (result.hasErrors()) {
            return "regist";
        }

        boolean idCheck = userService.findByloginId(userRegist.getLoginId()) != null;
        boolean passCheck = !userRegist.getPassword().equals(userRegist.getPassCheck());
        
        if (idCheck){
            model.addAttribute("titleError", "このログインIDは既に使用されています");
            return "regist";
        }

        if (passCheck) {
            model.addAttribute("titleError", "パスワードが一致しません");
            return "regist";
        }
        
        String loginId = userRegist.getLoginId();
        String password = userRegist.getPassword();
        String lastName = userRegist.getLastName();
        String firstName = userRegist.getFirstName();
        String gender = userRegist.getGender();
        String email = userRegist.getEmail() + userRegist.getDomain();
        Integer keyword1 = userRegist.getKeyword1();
        Integer keyword2 = userRegist.getKeyword2();
        Integer keyword3 = userRegist.getKeyword3();
        Integer roleId = loginId != null && loginId.contains("@5884") ? 1 : 2;

        MultipartFile image = userRegist.getUserImage();
        String imagePath = saveImage(image);

        UserDto userDto = new UserDto(loginId, password, lastName, firstName, gender, email, imagePath, keyword1,
                keyword2, keyword3, roleId);
        userService.insertUser(userDto);

        return "index";
    }

    /**
     * アップロードされた画像ファイルを保存し、保存先パスを返却する。
     *
     * @param image MultipartFile形式の画像ファイル
     * @return 保存された画像の相対パス。保存失敗時はnull。
     */
    private String saveImage(MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            try {
                String uploadDir = "src/main/resources/static/images/products/";
                String fileName = image.getOriginalFilename();
                Path path = Paths.get(uploadDir + fileName);
                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                return "/images/products/" + fileName;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

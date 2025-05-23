package jp.co.genproject.ettewebserve.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class UserController {
    
    @GetMapping("/mypage")
    public String mypage() {
        return "userView/userPage";
    }

    @GetMapping("/userUpdate")
    public String userUpdate() {
        return "userView/userUpdate";
    }
    
    
}

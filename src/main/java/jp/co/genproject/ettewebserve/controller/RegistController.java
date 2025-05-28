package jp.co.genproject.ettewebserve.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistController {

    @GetMapping("/regist")
    public String getMethodName() {
        return "regist";
    }

}

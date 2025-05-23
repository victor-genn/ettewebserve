package jp.co.genproject.ettewebserve.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {
    
    @GetMapping("/productList")
    public String productList() {
        return "productView/prodView";
    }

    @GetMapping("/productDetail")
    public String productDetail() {
        return "productView/prodDetail";
    }
    

    @GetMapping("/productRegist")
    public String productRegist() {
        return "productView/prodRegist";
    }

    @GetMapping("/productUpdate")
    public String productUpdate() {
        return "productView/prodUpdate";
    }
    
    
    
}

package jp.co.genproject.ettewebserve.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import jp.co.genproject.ettewebserve.form.PurchaseForm;
import jp.co.genproject.ettewebserve.service.PurchaseService;

@Controller
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final HttpSession session;

    public PurchaseController(PurchaseService purchaseService, HttpSession session) {
        this.purchaseService = purchaseService;
        this.session = session;
    }

    // 購入一覧（カート画面）
    @GetMapping("/purchaseList")
    public String purchaseList() {
        return "purchase/purcCargo";
    }

    // 購入処理
    @PostMapping("/purchase")
    public String purchase(@ModelAttribute("purchaseForm") PurchaseForm purchaseForm, Model model) {

        // Integer userId = (Integer) session.getAttribute("userId");
        Integer userId = 4;

        Integer productId = purchaseForm.getProductId();
        Integer sizeId = purchaseForm.getSizeId();
        Integer quantity = purchaseForm.getQuantity();
        Integer price = purchaseForm.getSalePrice();

        purchaseService.register(userId, productId, sizeId, quantity, price);

        model.addAttribute("message", "購入が完了しました。");
        return "purchase/purcCargo";
    }
}
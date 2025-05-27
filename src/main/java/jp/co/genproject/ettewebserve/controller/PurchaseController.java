package jp.co.genproject.ettewebserve.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import jp.co.genproject.ettewebserve.dto.CartViewDto;
import jp.co.genproject.ettewebserve.entity.Product;
import jp.co.genproject.ettewebserve.form.CartForm;
import jp.co.genproject.ettewebserve.form.ChoiceForm;
import jp.co.genproject.ettewebserve.form.PurchaseForm;
import jp.co.genproject.ettewebserve.service.ProductService;
import jp.co.genproject.ettewebserve.service.PurchaseService;

@Controller
public class PurchaseController {
    private final ProductService productService;
    private final PurchaseService purchaseService;
    private final HttpSession session;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    public PurchaseController(ProductService productService, PurchaseService purchaseService, HttpSession session) {
        this.productService = productService;
        this.purchaseService = purchaseService;
        this.session = session;
    }
    
    /**
     * 商品詳細画面で「カートに入れる」ボタンが押下された際の処理。
     * 対象商品をカートに登録し、再度商品詳細画面を表示する。
     *
     * @param cartForm カート登録用フォーム（商品ID、サイズ、数量などを保持）
     * @param model    ビューへデータを渡すためのモデル
     * @return 商品詳細画面のテンプレート名（productView/prodDetail）
     */
    @PostMapping("/cartIn")
    public String purchase(@ModelAttribute("cartForm") CartForm cartForm, Model model) {
        Product product = productService.findByProductId(cartForm.getProductId());
        model.addAttribute("product", product);

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) userId = 4;

        Integer productId = cartForm.getProductId();
        Integer sizeId = cartForm.getSizeId();
        Integer quantity = cartForm.getQuantity();

        purchaseService.addToCart(userId, productId, sizeId, quantity);

        String categoryName = productService.findCategoryNameByCategoryId(product.getCategoryId());
        String keywordName = productService.findKeywordNameByKeywordId(product.getKeywordId());
        String countryName = productService.findCountryNameByCountryId(product.getCountryId());

        model.addAttribute("categoryName", categoryName);
        model.addAttribute("keywordName", keywordName);
        model.addAttribute("countryName", countryName);

        model.addAttribute("cartForm", cartForm);
        return "productView/prodDetail";
    }

    // カートリスト
    @GetMapping("/cartList")
    public String cartList(@ModelAttribute("cartForm") CartForm cartForm, 
            @ModelAttribute("choiceForm") ChoiceForm choiceForm,
            @ModelAttribute PurchaseForm purchaseForm, Model model) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) userId = 4;

        List<CartViewDto> cartList = purchaseService.findCartViewByUserId(userId);
        for (CartViewDto item : cartList) {
            item.setAddedAtFormatted(item.getAddedAt().format(formatter));
        }

        model.addAttribute("cartList", cartList);
        model.addAttribute("purchaseForm", purchaseForm);

        return "purchase/purcCargo";
    }

    @PostMapping("/choiceItem")
    public String choiceItem(@ModelAttribute("choiceForm") ChoiceForm choiceForm,
            @ModelAttribute PurchaseForm purchaseForm, Model model, HttpSession session) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) userId = 4;

        List<CartViewDto> cartList = purchaseService.findCartViewByUserId(userId);
        for (CartViewDto item : cartList) {
            item.setAddedAtFormatted(item.getAddedAt().format(formatter));
        }

        model.addAttribute("cartList", cartList);

        List<Integer> selectedCartIds = choiceForm.getCartIds();

        List<CartViewDto> totalCartList = new ArrayList<>();
        Integer totalQuantity = 0;
        Integer totalPrice = 0;
        double taxIncludedPrice = 0;
        double point = 0;

        if (selectedCartIds != null && !selectedCartIds.isEmpty()) {
            totalCartList = purchaseService.purchaseFromCart(userId, selectedCartIds);
        }

        for(CartViewDto cart : totalCartList){
            totalQuantity += cart.getQuantity();
            totalPrice += cart.getQuantity()*cart.getSalePrice();
        }

        taxIncludedPrice = totalPrice*1.1;
        point = totalPrice*0.05;

        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("taxIncludedPrice", taxIncludedPrice);
        model.addAttribute("point", point);

        model.addAttribute("purchaseForm", purchaseForm);
        return "purchase/purcCargo";
    }

    @GetMapping("/purchaseHistory")
    public String showPurchaseForm(@ModelAttribute PurchaseForm purchaseForm, Model model) {
        model.addAttribute("purchaseForm", purchaseForm);
        return "purchase/purcHistory";
    }
    
    // 購入登録
    @PostMapping("/purchase")
    public String registerPurchase(@ModelAttribute PurchaseForm purchaseForm, Model model, HttpSession session) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) userId = 4;

        List<Integer> cartIds = purchaseForm.getCartIds();
        Integer totalQuantity = purchaseForm.getTotalQuantity();
        Integer totalPrice = purchaseForm.getTotalPrice();

        purchaseService.registerPurchase(userId, cartIds, totalQuantity, totalPrice);

        model.addAttribute("purchaseList", purchaseList);
        return "purchase/purcHistory"; 
    }
}
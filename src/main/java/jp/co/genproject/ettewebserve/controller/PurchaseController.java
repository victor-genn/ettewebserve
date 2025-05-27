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
import jp.co.genproject.ettewebserve.entity.PurchaseHistory;
import jp.co.genproject.ettewebserve.form.CartForm;
import jp.co.genproject.ettewebserve.form.ChoiceForm;
import jp.co.genproject.ettewebserve.form.PurchaseForm;
import jp.co.genproject.ettewebserve.form.PurchaseSearchForm;
import jp.co.genproject.ettewebserve.service.ProductService;
import jp.co.genproject.ettewebserve.service.PurchaseService;

/**
 * 購入関連機能のコントローラークラス。
 * カートへの商品追加、カート一覧の表示、購入確認および購入処理、購入履歴の表示を担当する。
 *
 * 主な機能：
 * 商品詳細ページからカートへの追加、カート内容の表示と選択商品の購入確認、
 * 購入処理の実行、購入履歴画面の初期表示などを行う。
 *
 * 使用技術：
 * Spring MVC と Thymeleaf テンプレートエンジンを使用。
 *
 * @author 張勝現
 * @version 1.0
 */
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

    /**
     * カートリスト画面を表示する処理です。
     *
     * @param cartForm カート情報を保持するフォームオブジェクト
     * @param choiceForm ユーザーが選択した商品の情報を保持するフォームオブジェクト
     * @param purchaseForm 購入処理に使用するフォームオブジェクト
     * @param model 画面にデータを渡すためのModelオブジェクト
     * @return カートリスト画面のテンプレート名（purchase/purcCargo）
     */
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

    /**
     * カート内でユーザーが選択した商品の購入確認情報を表示します。
     *
     * @param choiceForm 選択されたカートIDのリストを含むフォームオブジェクト
     * @param purchaseForm 購入処理に使用するフォームオブジェクト
     * @param model 画面にデータを渡すためのModelオブジェクト
     * @param session ユーザーセッション情報
     * @return 購入確認画面のテンプレート名（purchase/purcCargo）
     */
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

    /**
     * 購入履歴検索画面を表示する処理です。
     *
     * @param purchaseForm 購入履歴検索に使用するフォームオブジェクト
     * @param model 画面にデータを渡すためのModelオブジェクト
     * @return 購入履歴画面のテンプレート名（purchase/purcHistory）
     */
    @GetMapping("/purchaseHistory")
    public String purchaseHistory(@ModelAttribute PurchaseForm purchaseForm, Model model) {
        model.addAttribute("purchaseForm", purchaseForm);
        return "purchase/purcHistory";
    }

    /**
     * 購入履歴検索フォームの送信処理。
     * 検索条件または並び順に応じて購入履歴をフィルターまたはソートし、結果をビューに表示する。
     *
     * 主な機能：
     * ・検索条件（商品名／金額）による履歴フィルター処理  
     * ・並び順（日付／数量／金額）による履歴ソート処理  
     * ・セッションからユーザーIDを取得し、ユーザー別の購入履歴を取得  
     * ・検索フォームの入力値と検索結果をモデルに追加し、画面へ返却
     *
     * @param searchForm 検索条件・並び順などのフォーム情報  
     * @param session ログインユーザー情報の取得に使用  
     * @param model 検索結果とフォーム情報をビューに渡すためのモデル  
     * @return 購入履歴画面のパス（purchase/purcHistory）
     */
    @PostMapping("/purchaseHistory")
    public String postPurchaseHistory(@ModelAttribute("searchForm") PurchaseSearchForm searchForm, HttpSession session, Model model) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) userId = 4;

        List<PurchaseHistory> purchaseList;

        boolean hasKeyword = searchForm.getSearchKeyword() != null && !searchForm.getSearchKeyword().isEmpty();
        boolean hasCondition = searchForm.getSearchCondition() != null && !searchForm.getSearchCondition().isEmpty();
        boolean hasSort = searchForm.getSortBy() != null && !searchForm.getSortBy().isEmpty();

        if (hasCondition && hasKeyword) {
            if ("productName".equals(searchForm.getSearchCondition()) && hasKeyword) {
                purchaseList = purchaseService.findPurchaseByProducName(searchForm.getSearchKeyword());
            } else if ("price".equals(searchForm.getSearchCondition())) {
                purchaseList = purchaseService.findPurchaseByPrice(Integer.parseInt(searchForm.getSearchKeyword()));
            } else {
                purchaseList = purchaseService.findPurchaseByUserId(userId);
            }
        } else if (hasSort) {
            switch (searchForm.getSortBy()) {
                case "date":
                    purchaseList = purchaseService.sortPurchaseByDate();
                    break;
                case "quantity":
                    purchaseList = purchaseService.sortPurchaseByQuantity();
                    break;
                case "price":
                    purchaseList = purchaseService.sortPurchaseByPrice();
                    break;
                default:
                    purchaseList = purchaseService.findPurchaseByUserId(userId);
            }
        } else {
            purchaseList = purchaseService.findPurchaseByUserId(userId);
        }

        model.addAttribute("purchaseList", purchaseList);
        model.addAttribute("searchForm", searchForm);
        return "purchase/purcHistory";
    }

    /**
     * 選択されたカート商品の購入処理を実行し、購入履歴画面を表示します。
     *
     * @param purchaseForm 購入情報（カートIDリスト、数量、価格）を含むフォームオブジェクト
     * @param model 画面にデータを渡すためのModelオブジェクト
     * @param session ユーザーセッション情報
     * @return 購入履歴画面のテンプレート名（purchase/purcHistory）
     */
    @PostMapping("/purchase")
    public String registerPurchase(@ModelAttribute PurchaseForm purchaseForm, Model model, HttpSession session) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) userId = 4;

        List<Integer> cartIds = purchaseForm.getCartIds();
        Integer totalQuantity = purchaseForm.getTotalQuantity();
        Integer totalPrice = purchaseForm.getTotalPrice();

        purchaseService.registerPurchase(userId, cartIds, totalQuantity, totalPrice);

        List<PurchaseHistory> purchaseList = purchaseService.findPurchaseByUserId(userId);
        model.addAttribute("purchaseList", purchaseList);
        return "purchase/purcHistory"; 
    }
}
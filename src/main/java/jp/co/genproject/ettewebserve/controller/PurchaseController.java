package jp.co.genproject.ettewebserve.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
 * カート操作、購入処理、購入履歴の表示・検索・削除など、
 * 購入に関する画面制御を担当する。
 *
 * 主な機能：
 * ・商品詳細ページからのカート追加
 * ・カート一覧表示および購入確認情報の表示
 * ・選択商品の購入処理および購入履歴登録
 * ・購入履歴の表示、検索、並び替え、削除
 *
 * 使用技術：
 * ・Spring MVC
 * ・Thymeleaf テンプレートエンジン
 * ・フォームオブジェクトを用いたデータ受け渡し
 * ・セッション管理
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
     * 商品詳細画面からカートに商品を追加する処理。
     *
     * @param cartForm カート登録用フォーム
     * @param model    ビューに渡すモデル
     * @return 商品詳細画面テンプレート
     */
    @PostMapping("/cartIn")
    public String purchase(@ModelAttribute("cartForm") CartForm cartForm, Model model) {
        Product product = productService.findByProductId(cartForm.getProductId());
        model.addAttribute("product", product);

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null)
            userId = 4;

        purchaseService.addToCart(userId, cartForm.getProductId(), cartForm.getSizeId(), cartForm.getQuantity());

        model.addAttribute("categoryName", productService.findCategoryNameByCategoryId(product.getCategoryId()));
        model.addAttribute("keywordName", productService.findKeywordNameByKeywordId(product.getKeywordId()));
        model.addAttribute("countryName", productService.findCountryNameByCountryId(product.getCountryId()));
        model.addAttribute("cartForm", cartForm);

        return "productView/prodDetail";
    }

    /**
     * カート一覧画面を表示する処理。
     *
     * @param cartForm     カートフォーム
     * @param choiceForm   選択情報フォーム
     * @param purchaseForm 購入フォーム
     * @param model        ビューに渡すモデル
     * @return カート画面テンプレート
     */
    @GetMapping("/cartList")
    public String cartList(@ModelAttribute("cartForm") CartForm cartForm,
            @ModelAttribute("choiceForm") ChoiceForm choiceForm,
            @ModelAttribute PurchaseForm purchaseForm,
            Model model) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null)
            userId = 4;

        List<CartViewDto> cartList = purchaseService.findCartViewByUserId(userId);
        for (CartViewDto item : cartList) {
            item.setAddedAtFormatted(item.getAddedAt().format(formatter));
        }

        model.addAttribute("cartList", cartList);
        model.addAttribute("purchaseForm", purchaseForm);

        return "purchase/purcCargo";
    }

    /**
     * 選択したカート商品を購入確認画面に表示する処理。
     *
     * @param choiceForm   選択されたカートIDのリスト
     * @param purchaseForm 購入フォーム
     * @param model        ビューに渡すモデル
     * @param session      ユーザーセッション
     * @return 確認画面テンプレート
     */
    @PostMapping("/choiceItem")
    public String choiceItem(@ModelAttribute("choiceForm") ChoiceForm choiceForm,
            @ModelAttribute PurchaseForm purchaseForm,
            Model model, HttpSession session) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null)
            userId = 4;

        List<CartViewDto> cartList = purchaseService.findCartViewByUserId(userId);
        for (CartViewDto item : cartList) {
            item.setAddedAtFormatted(item.getAddedAt().format(formatter));
        }
        model.addAttribute("cartList", cartList);

        List<Integer> selectedCartIds = choiceForm.getCartIds();
        List<CartViewDto> totalCartList = new ArrayList<>();
        Integer totalQuantity = 0;
        Integer totalPrice = 0;

        if (selectedCartIds != null && !selectedCartIds.isEmpty()) {
            totalCartList = purchaseService.purchaseFromCart(userId, selectedCartIds);
            purchaseForm.setCartIds(selectedCartIds);
        }

        for (CartViewDto cart : totalCartList) {
            totalQuantity += cart.getQuantity();
            totalPrice += cart.getQuantity() * cart.getSalePrice();
        }

        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("taxIncludedPrice", totalPrice * 1.1);
        model.addAttribute("point", totalPrice * 0.05);
        model.addAttribute("purchaseForm", purchaseForm);

        return "purchase/purcCargo";
    }

    /**
     * 購入履歴画面の初期表示処理。
     *
     * @param purchaseForm 購入フォーム
     * @param searchForm   検索フォーム
     * @param model        ビューに渡すモデル
     * @param session      セッション情報
     * @return 履歴画面テンプレート
     */
    @GetMapping("/purchaseHistory")
    public String purchaseHistory(@ModelAttribute PurchaseForm purchaseForm,
            @ModelAttribute("searchForm") PurchaseSearchForm searchForm,
            Model model, HttpSession session) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null)
            userId = 4;

        List<PurchaseHistory> purchaseList = purchaseService.findPurchaseByUserId(userId);

        model.addAttribute("searchForm", searchForm);
        model.addAttribute("purchaseForm", purchaseForm);
        model.addAttribute("purchaseList", purchaseList);
        return "purchase/purcHistory";
    }

    /**
     * 購入履歴の検索および並び替え処理。
     *
     * @param searchForm 検索条件フォーム
     * @param session    セッション情報
     * @param model      ビューに渡すモデル
     * @return 履歴画面テンプレート
     */
    @PostMapping("/purchaseHistory")
    public String postPurchaseHistory(@ModelAttribute("searchForm") PurchaseSearchForm searchForm,
            HttpSession session, Model model) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null)
            userId = 4;

        List<PurchaseHistory> purchaseList;

        boolean hasKeyword = searchForm.getSearchKeyword() != null && !searchForm.getSearchKeyword().isEmpty();
        boolean hasCondition = searchForm.getSearchCondition() != null && !searchForm.getSearchCondition().isEmpty();
        boolean hasSort = searchForm.getSortBy() != null && !searchForm.getSortBy().isEmpty();

        if (hasCondition && hasKeyword) {
            if ("productName".equals(searchForm.getSearchCondition())) {
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
        return "purchase/purcHistory";
    }

    /**
     * カート選択商品の購入処理。
     *
     * @param purchaseForm 購入情報を含むフォーム
     * @param model        ビューに渡すモデル
     * @param session      セッション情報
     * @return 購入履歴画面へリダイレクト
     */
    @PostMapping("/purchase")
    public String registerPurchase(@ModelAttribute PurchaseForm purchaseForm, Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null)
            userId = 4;

        List<Integer> cartIdList = purchaseForm.getCartIds();
        Integer totalQuantity = purchaseForm.getTotalQuantity();
        Integer totalPrice = purchaseForm.getTotalPrice();

        if (cartIdList == null || cartIdList.isEmpty()) {
            model.addAttribute("error", "購入する商品が選択されていません。");
            return "purchase/purcCargo";
        }

        purchaseService.registerPurchase(userId, cartIdList, totalQuantity, totalPrice);
        return "redirect:/purchaseHistory";
    }

    /**
     * 選択された購入履歴の削除処理。
     *
     * @param selectedIds 削除対象の購入履歴IDリスト
     * @param session     セッション情報
     * @return 購入履歴画面へリダイレクト
     */
    @PostMapping("/purchaseHistory/delete")
    public String deletePurchaseHistory(
            @RequestParam(value = "selectedIds", required = false) List<Integer> selectedIds,
            HttpSession session) {
        if (selectedIds != null && !selectedIds.isEmpty()) {
            purchaseService.deletePurchaseHistories(selectedIds);
        }
        return "redirect:/purchaseHistory";
    }
}

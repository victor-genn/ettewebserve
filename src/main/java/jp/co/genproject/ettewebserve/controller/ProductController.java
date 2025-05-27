package jp.co.genproject.ettewebserve.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.genproject.ettewebserve.entity.Product;
import jp.co.genproject.ettewebserve.form.CartForm;
import jp.co.genproject.ettewebserve.form.ProductForm;
import jp.co.genproject.ettewebserve.service.ProductService;

/**
 * 商品関連機能のコントローラークラス。
 * 商品一覧表示、検索、詳細表示、登録・更新画面遷移など、
 * 商品に関する画面制御を担当する。
 *
 * 主な機能：
 * ・商品一覧の初期表示  
 * ・カテゴリー、商品名、キーワードによる検索  
 * ・商品詳細画面の表示  
 * ・商品登録・更新画面への遷移
 *
 * 使用技術：
 * ・Spring MVC  
 * ・Thymeleaf テンプレートエンジン  
 * ・フォームオブジェクトによる入力値バインディング
 *
 * @author 張勝現
 * @version 1.0
 */
@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    /**
     * 商品一覧画面の初期表示処理。
     *
     * @param productForm 検索条件フォーム
     * @param model ビューに渡すモデル
     * @return 商品一覧画面テンプレート
     */
    @GetMapping("/productList")
    public String productList(@ModelAttribute("productForm") ProductForm productForm, Model model) {
        productForm.setSearchType("productName");
        productForm.setSortOrder("created_at");

        List<Product> productList = productService.findByAll();
        model.addAttribute("productList", productList);

        List<Product> recommendList = productService.findByRecommends();
        model.addAttribute("recommendList", recommendList);

        model.addAttribute("productForm", productForm);
        return "productView/prodView";
    }

    /**
     * 商品検索処理。
     *
     * @param productForm 入力された検索条件フォーム
     * @param result バリデーション結果
     * @param model ビューに渡すモデル
     * @return 商品一覧画面テンプレート
     */
    @PostMapping("/productSearch")
    public String productSearch(@Validated @ModelAttribute("productForm") ProductForm productForm, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "productView/prodView";
        }

        Integer categoryId = productForm.getCategoryId();
        String searchType = productForm.getSearchType();
        String searchWord = productForm.getSearchWord();
        String sortOrder = productForm.getSortOrder();

        List<Product> productList;

        boolean categoryIdNull = categoryId == null;
        boolean searchTypeEmpty = (searchType == null || searchType.trim().isEmpty());
        boolean searchWordEmpty = (searchWord == null || searchWord.trim().isEmpty());

        if (categoryIdNull && searchTypeEmpty && searchWordEmpty) {
            productList = productService.findByAll();
        } else if (!categoryIdNull && searchTypeEmpty && searchWordEmpty) {
            productList = productService.findByCategory(categoryId, sortOrder);
        } else if (categoryIdNull && !searchTypeEmpty && !searchWordEmpty) {
            if ("productName".equals(searchType)) {
                productList = productService.findByProductName(searchWord, sortOrder);
            } else if ("keyword".equals(searchType)) {
                productList = productService.findByKeywordName(searchWord, sortOrder);
            } else {
                productList = productService.findByAll();
            }
        } else if (!categoryIdNull && !searchTypeEmpty && !searchWordEmpty) {
            if ("productName".equals(searchType)) {
                productList = productService.findByCategoryAndProductName(categoryId, searchWord, sortOrder);
            } else if ("keyword".equals(searchType)) {
                productList = productService.findByCategoryAndKeyword(categoryId, searchWord, sortOrder);
            } else {
                productList = productService.findByAll();
            }
        } else {
            productList = productService.findByAll();
        }

        model.addAttribute("productList", productList);
        model.addAttribute("productForm", productForm);
        model.addAttribute("totalPages", 5); // ページネーション対応を想定した固定値
        return "productView/prodView";
    }

    /**
     * 商品詳細画面の表示処理。
     *
     * @param cartForm カート追加用フォーム
     * @param productId 表示対象の商品ID
     * @param model ビューに渡すモデル
     * @return 商品詳細画面テンプレート
     */
    @PostMapping("/productDetail")
    public String productDetail(@ModelAttribute("cartForm") CartForm cartForm,
                                @RequestParam("productId") int productId, Model model) {

        Product product = productService.findByProductId(productId);
        model.addAttribute("product", product);

        model.addAttribute("categoryName", productService.findCategoryNameByCategoryId(product.getCategoryId()));
        model.addAttribute("keywordName", productService.findKeywordNameByKeywordId(product.getKeywordId()));
        model.addAttribute("countryName", productService.findCountryNameByCountryId(product.getCountryId()));

        return "productView/prodDetail";
    }

    /**
     * 商品登録画面への遷移処理。
     *
     * @return 登録画面テンプレート
     */
    @GetMapping("/productRegist")
    public String productRegist() {
        return "productView/prodRegist";
    }

    /**
     * 商品更新画面への遷移処理。
     *
     * @return 更新画面テンプレート
     */
    @GetMapping("/productUpdate")
    public String productUpdate() {
        return "productView/prodUpdate";
    }
}

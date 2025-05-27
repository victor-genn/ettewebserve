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
 * 商品一覧表示、商品検索、商品詳細表示、商品登録・更新画面への遷移処理を担当する。
 *
 * 主な機能：
 * 商品一覧ページの初期表示、検索フォームによる商品検索、商品詳細の表示、
 * 商品登録・更新ページへの遷移などを行う。
 *
 * 使用技術：
 * Spring MVC と Thymeleaf テンプレートエンジンを使用。
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
     * 商品一覧画面を初期表示する。
     * 検索フォームにデフォルトの値（商品名・新着順）を設定し、
     * 全商品リストおよび推薦商品リストを取得して画面に渡す。
     *
     * @param productForm 検索条件を保持するフォームオブジェクト
     * @param model       ビューに渡すデータを格納するモデル
     * @return 商品一覧画面のパス（productView/prodView）
     */
    @GetMapping("/productList")
    public String productList(@ModelAttribute("productForm") ProductForm productForm, Model model) {
        // 自動選択
        productForm.setSearchType("productName");
        productForm.setSortOrder("created_at");

        // 画面遷移後、全体リスト出力
        List<Product> productList = productService.findByAll();
        model.addAttribute("productList", productList);

        // 画面遷移後、推薦商品リスト出力
        List<Product> recommendList = productService.findByRecommends();
        model.addAttribute("recommendList", recommendList);

        // フォームバインディング
        model.addAttribute("productForm", productForm);
        return "productView/prodView";
    }

    /**
     * 商品検索を実行し、検索結果を一覧画面に表示する。
     * 検索条件（カテゴリー・商品名・キーワードなど）に基づき、
     * 条件に応じた商品リストを取得して画面に渡す。
     *
     * @param productForm ユーザーが入力した検索条件を保持するフォームオブジェクト
     * @param result      バリデーション結果を保持するオブジェクト
     * @param model       ビューに渡すデータを格納するモデル
     * @return 商品一覧画面のパス（productView/prodView）
     */
    @PostMapping("/productSearch")
    public String productSearch(@Validated @ModelAttribute("productForm") ProductForm productForm, BindingResult result, Model model) {

        // validate処理
        if(result.hasErrors()){
            return "productView/prodView";
        }

        // 入力値を持ってくる
        Integer categoryId = productForm.getCategoryId();
        String searchType = productForm.getSearchType();
        String searchWord = productForm.getSearchWord();
        String sortOrder = productForm.getSortOrder();

        List<Product> productList;

        // 条件変数
        boolean categoryIdBoolean = categoryId == null;
        boolean searchTypeBoolean = (searchType == null || searchType.trim().isEmpty());
        boolean searchWordBoolean = (searchWord == null || searchWord.trim().isEmpty());

        // default
        if (categoryIdBoolean && searchTypeBoolean && searchWordBoolean) {
            productList = productService.findByAll();

        // カテゴリーだけで検索
        } else if (!categoryIdBoolean && searchTypeBoolean && searchWordBoolean) {
            productList = productService.findByCategory(categoryId, sortOrder);

        // 検索条件および検索語で検索
        } else if (categoryIdBoolean && !searchTypeBoolean && !searchWordBoolean){
            if ("productName".equals(searchType)) {
                productList = productService.findByProductName(searchWord, sortOrder);
            } else if ("keyword".equals(searchType)) {
                productList = productService.findByKeywordName(searchWord, sortOrder);
            } else {
                productList = productService.findByAll();
            }

        // カテゴリーおよび検索語で検索
        } else if(!categoryIdBoolean && !searchTypeBoolean && !searchWordBoolean) {
            if ("productName".equals(searchType)) {
                productList = productService.findByCategoryAndProductName(categoryId, searchWord, sortOrder);
            } else if ("keyword".equals(searchType)) {
                productList = productService.findByCategoryAndKeyword(categoryId, searchWord, sortOrder);
            } else {
                productList = productService.findByAll();
            }
        } else{
            productList = productService.findByAll();
        }

        // 画面に商品リスト出力
        model.addAttribute("productList", productList);
        model.addAttribute("productForm", productForm);
        model.addAttribute("totalPages", 5);
        return "productView/prodView";
    }

    /**
     * 商品詳細ページを表示する処理。
     * 画面遷移前に、指定された商品IDに基づいて商品情報および関連情報（カテゴリ名、キーワード名、製造国名）を取得し、モデルに追加する。
     *
     * @param cartForm カート追加用フォームオブジェクト（バインディング用）
     * @param productId 表示対象の商品ID
     * @param model ビューへデータを渡すためのモデル
     * @return 商品詳細ページのテンプレート名
     */
    @PostMapping("/productDetail")
    public String productDetail(@ModelAttribute("cartForm") CartForm cartForm, @RequestParam("productId") int productId, Model model) {
        
        // 商品IDをもとに商品情報を取得
        Product product = productService.findByProductId(productId);
        model.addAttribute("product", product);

        // 商品に関連する各名称を取得してモデルに追加
        String categoryName = productService.findCategoryNameByCategoryId(product.getCategoryId());
        String keywordName = productService.findKeywordNameByKeywordId(product.getKeywordId());
        String countryName = productService.findCountryNameByCountryId(product.getCountryId());

        model.addAttribute("categoryName", categoryName);
        model.addAttribute("keywordName", keywordName);
        model.addAttribute("countryName", countryName);

        // 商品詳細画面に遷移
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

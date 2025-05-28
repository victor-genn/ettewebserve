package jp.co.genproject.ettewebserve.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.genproject.ettewebserve.dto.ProductDto;
import jp.co.genproject.ettewebserve.entity.Product;
import jp.co.genproject.ettewebserve.form.CartForm;
import jp.co.genproject.ettewebserve.form.ProductForm;
import jp.co.genproject.ettewebserve.form.ProductRegistForm;
import jp.co.genproject.ettewebserve.form.ProductUpdateForm;
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

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 商品一覧画面の初期表示処理。
     *
     * @param productForm 検索条件フォーム
     * @param model       ビューに渡すモデル
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
     * @param result      バリデーション結果
     * @param model       ビューに渡すモデル
     * @return 商品一覧画面テンプレート
     */
    @PostMapping("/productSearch")
    public String productSearch(@Validated @ModelAttribute("productForm") ProductForm productForm, BindingResult result,
            Model model) {

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
                productList = productService.findByCategoryAndProductName(categoryId, sortOrder, searchWord);
            } else if ("keyword".equals(searchType)) {
                productList = productService.findByCategoryAndKeyword(categoryId, sortOrder, searchWord);
            } else {
                productList = productService.findByAll();
            }
        } else {
            productList = productService.findByAll();
        }
        
        List<Product> recommendList = productService.findByRecommends();
        model.addAttribute("recommendList", recommendList);
        model.addAttribute("productList", productList);
        model.addAttribute("productForm", productForm);
        return "productView/prodView";
    }

    /**
     * 商品詳細画面の表示処理。
     *
     * @param cartForm  カート追加用フォーム
     * @param productId 表示対象の商品ID
     * @param model     ビューに渡すモデル
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
     * 商品登録画面の初期表示処理。
     * 入力フォームおよびマスターデータ（カテゴリー、キーワード、製造国）を初期化してビューに渡す。
     *
     * @param productRegist 商品登録フォームオブジェクト
     * @param model         ビューに渡すモデル
     * @return 商品登録画面テンプレート
     */
    @GetMapping("/productRegist")
    public String productRegist(@ModelAttribute("productRegistForm") ProductRegistForm productRegist, Model model) {
        model.addAttribute("categoryList", productService.findAllCategory());
        model.addAttribute("keywordList", productService.findAllKeyword());
        model.addAttribute("countryList", productService.findAllCountry());
        return "productView/prodRegist";
    }

    /**
     * 商品登録処理。
     *
     * 入力された商品情報をバリデーション検証後、画像ファイルを保存し、
     * DTOに変換してデータベースへ登録する。
     * 入力エラーがある場合は登録画面に戻り、エラーメッセージを表示する。
     *
     * 主な処理内容：
     * ・フォーム入力値のバリデーションチェック
     * ・画像ファイルの保存（/static/images/products に保存）
     * ・フォームデータを ProductDto に変換
     * ・ProductService を通じて DBに登録
     * ・登録完了後、商品一覧画面に遷移
     *
     * @param productRegist 商品登録フォーム（バリデーション対象）
     * @param result        バリデーション結果
     * @param model         ビューに渡すモデル
     * @return 商品一覧画面テンプレート（またはバリデーションエラー時は登録画面）
     */
    @PostMapping("/productRegist")
    public String productIncert(@Validated @ModelAttribute("productRegistForm") ProductRegistForm productRegist,
            @ModelAttribute("productForm") ProductForm productForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categoryList", productService.findAllCategory());
            model.addAttribute("keywordList", productService.findAllKeyword());
            model.addAttribute("countryList", productService.findAllCountry());
            return "productView/prodRegist";
        }

        model.addAttribute("productRegistForm", productRegist);

        model.addAttribute("categoryList", productService.findAllCategory());
        model.addAttribute("keywordList", productService.findAllKeyword());
        model.addAttribute("countryList", productService.findAllCountry());

        Integer categoryId = productRegist.getCategoryId();
        String productName = productRegist.getProductName();
        Integer keywordId = productRegist.getKeywordId();
        Integer countryId = productRegist.getCountryId();
        String manufactureDate = productRegist.getManufactureDate();
        String description = productRegist.getDescription();
        Integer stockS = productRegist.getStockS();
        Integer stockM = productRegist.getStockM();
        Integer stockL = productRegist.getStockL();
        Integer stockXL = productRegist.getStockXL();
        Integer regularPrice = productRegist.getRegularPrice();
        Integer discountRate = productRegist.getDiscountRate();
        Integer salesPrice = (int) (regularPrice * (1 - discountRate * 0.01));

        MultipartFile image = productRegist.getProductImage();
        String imagePath = saveImage(image);

        ProductDto productDto = new ProductDto(
                productName, categoryId, keywordId, countryId,
                manufactureDate, regularPrice, discountRate, salesPrice,
                stockS, stockM, stockL, stockXL, imagePath, description);

        productService.insertProduct(productDto);

        model.addAttribute("productList", productService.findByAll());
        model.addAttribute("recommendList", productService.findByRecommends());

        return "productView/prodView";
    }

    @GetMapping("/productUpdate")
    public String productDirection(@RequestParam("productId") Integer productId, Model model) {
        Product product = productService.findByProductId(productId);

        ProductUpdateForm updateFormform = new ProductUpdateForm();

        updateFormform.setProductId(product.getProductId());
        updateFormform.setProductName(product.getProductName());
        updateFormform.setCategoryId(product.getCategoryId());
        updateFormform.setKeywordId(product.getKeywordId());
        updateFormform.setCountryId(product.getCountryId());
        updateFormform.setManufactureDate(product.getManufactureDate());
        updateFormform.setDescription(product.getDescription());
        updateFormform.setStockS(product.getStockS());
        updateFormform.setStockM(product.getStockM());
        updateFormform.setStockL(product.getStockL());
        updateFormform.setStockXL(product.getStockXL());
        updateFormform.setRegularPrice(product.getRegularPrice());
        updateFormform.setDiscountRate(product.getDiscountRate());
        updateFormform.setSalePrice(product.getSalePrice());
        updateFormform.setImagePath(product.getImagePath());

        updateFormform.setStockS(product.getStockS() == null ? 0 : product.getStockS());
        updateFormform.setStockM(product.getStockM() == null ? 0 : product.getStockM());
        updateFormform.setStockL(product.getStockL() == null ? 0 : product.getStockL());
        updateFormform.setStockXL(product.getStockXL() == null ? 0 : product.getStockXL());

        updateFormform.setRegularPrice(product.getRegularPrice());
        updateFormform.setDiscountRate(product.getDiscountRate());
        updateFormform.setSalePrice(product.getSalePrice());
        updateFormform.setImagePath(product.getImagePath());

        model.addAttribute("productUpdateForm", updateFormform);
        model.addAttribute("categoryList", productService.findAllCategory());
        model.addAttribute("keywordList", productService.findAllKeyword());
        model.addAttribute("countryList", productService.findAllCountry());

        return "productView/prodUpdate";
    }

    /**
     * 商品更新画面への遷移処理。
     *
     * @return 更新画面テンプレート
     */
    @PostMapping("/productUpdate")
    public String productUpdate(
            @Validated @ModelAttribute("productUpdateForm") ProductUpdateForm productUpdateForm,
            @ModelAttribute("productForm") ProductForm productForm, BindingResult result, Model model) {

        if (productUpdateForm.getStockS() == null)
            productUpdateForm.setStockS(0);
        if (productUpdateForm.getStockM() == null)
            productUpdateForm.setStockM(0);
        if (productUpdateForm.getStockL() == null)
            productUpdateForm.setStockL(0);
        if (productUpdateForm.getStockXL() == null)
            productUpdateForm.setStockXL(0);

        if (result.hasErrors()) {
            model.addAttribute("categoryList", productService.findAllCategory());
            model.addAttribute("keywordList", productService.findAllKeyword());
            model.addAttribute("countryList", productService.findAllCountry());
            return "productView/prodUpdate";
        }

        model.addAttribute("categoryList", productService.findAllCategory());
        model.addAttribute("keywordList", productService.findAllKeyword());
        model.addAttribute("countryList", productService.findAllCountry());

        Integer productId = productUpdateForm.getProductId();
        String productName = productUpdateForm.getProductName();
        Integer categoryId = productUpdateForm.getCategoryId();
        Integer keywordId = productUpdateForm.getKeywordId();
        Integer countryId = productUpdateForm.getCountryId();
        String manufactureDate = productUpdateForm.getManufactureDate();
        String description = productUpdateForm.getDescription();
        Integer stockS = productUpdateForm.getStockS();
        Integer stockM = productUpdateForm.getStockM();
        Integer stockL = productUpdateForm.getStockL();
        Integer stockXL = productUpdateForm.getStockXL();
        Integer regularPrice = productUpdateForm.getRegularPrice();
        Integer discountRate = productUpdateForm.getDiscountRate();
        if (discountRate == null)
            discountRate = 0;
        Integer salesPrice = (int) (regularPrice * (1 - discountRate * 0.01));

        String imagePath = productUpdateForm.getImagePath();
        MultipartFile image = productUpdateForm.getProductImage();

        if (image != null && !image.isEmpty()) {
            imagePath = saveImage(image);
        }

        if (imagePath == null || imagePath.isEmpty()) {
            Product existing = productService.findByProductId(productUpdateForm.getProductId());
            imagePath = existing.getImagePath();
        }

        ProductDto productDto = new ProductDto(
                productId, productName, categoryId, keywordId, countryId,
                manufactureDate, regularPrice, discountRate, salesPrice,
                stockS, stockM, stockL, stockXL, imagePath, description);

        productService.updateProduct(productDto);

        model.addAttribute("productList", productService.findByAll());
        model.addAttribute("recommendList", productService.findByRecommends());

        return "productView/prodView";
    }

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
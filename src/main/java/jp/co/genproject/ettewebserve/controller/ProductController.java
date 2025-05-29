package jp.co.genproject.ettewebserve.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
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
    private final HttpSession session;

    public ProductController(ProductService productService, HttpSession session) {
        this.productService = productService;
        this.session = session;
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

        Integer userId = getSessionInt(session, "userId");

        if (userId == null) {
            return "index";
        }

        List<Product> productList = productService.findByAll();
        List<Product> recommendList = productService.findByRecommends();

        model.addAttribute("productList", productList);
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

        Integer userId = getSessionInt(session, "userId");

        if (userId == null) {
            return "index";
        }

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

        Integer userId = getSessionInt(session, "userId");

        if (userId == null) {
            return "index";
        }

        Product product = productService.findByProductId(productId);
        model.addAttribute("product", product);

        model.addAttribute("categoryName", productService.findCategoryNameByCategoryId(product.getCategoryId()));
        model.addAttribute("keywordName", productService.findKeywordNameByKeywordId(product.getKeywordId()));
        model.addAttribute("countryName", productService.findCountryNameByCountryId(product.getCountryId()));

        return "productView/prodDetail";
    }

    /**
     * 商品登録画面の初期表示処理。
     *
     * @param productRegist 商品登録フォームオブジェクト
     * @param model         ビューに渡すモデル
     * @return 商品登録画面テンプレート
     */
    @GetMapping("/productRegist")
    public String productRegist(@ModelAttribute("productRegistForm") ProductRegistForm productRegist, Model model) {

        Integer userId = getSessionInt(session, "userId");
        Integer roleId = getSessionInt(session, "roleId");

        if (userId == null || roleId == 2) {
            return "index";
        }

        model.addAttribute("categoryList", productService.findAllCategory());
        model.addAttribute("keywordList", productService.findAllKeyword());
        model.addAttribute("countryList", productService.findAllCountry());

        return "productView/prodRegist";
    }

    /**
     * 商品登録処理。
     * 入力された商品情報をバリデーション検証後、画像ファイルを保存し、
     * DTOに変換してデータベースへ登録する。
     *
     * @param productRegist 商品登録フォーム（バリデーション対象）
     * @param productForm   商品検索フォーム（画面に再表示するための補助用）
     * @param result        バリデーション結果
     * @param model         モデルオブジェクト（画面表示用）
     * @return 商品一覧画面テンプレート、またはエラー時は登録画面
     */
    @PostMapping("/productRegist")
    public String productIncert(@Validated @ModelAttribute("productRegistForm") ProductRegistForm productRegist,
            @ModelAttribute("productForm") ProductForm productForm,
            BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        Integer userId = getSessionInt(session, "userId");
        Integer roleId = getSessionInt(session, "roleId");

        if (userId == null || roleId == 2) {
            return "index";
        }

        if (result.hasErrors()) {
            model.addAttribute("categoryList", productService.findAllCategory());
            model.addAttribute("keywordList", productService.findAllKeyword());
            model.addAttribute("countryList", productService.findAllCountry());
            return "productView/prodRegist";
        }

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

        ProductDto productDto = new ProductDto(productName, categoryId, keywordId, countryId,
                manufactureDate, regularPrice, discountRate, salesPrice,
                stockS, stockM, stockL, stockXL, imagePath, description);

        productService.insertProduct(productDto);

        model.addAttribute("productList", productService.findByAll());
        model.addAttribute("recommendList", productService.findByRecommends());

        redirectAttributes.addFlashAttribute("successRegistProduct", "「" + productName + "」商品の登録を完了しました。");

        return "redirect:/productList";
    }

    /**
     * 商品更新画面の初期表示。
     *
     * @param productId 対象商品のID
     * @param model     モデルオブジェクト（画面表示用）
     * @return 商品更新画面テンプレート
     */
    @GetMapping("/productUpdate")
    public String productDirection(@RequestParam("productId") Integer productId, Model model) {

        Integer userId = getSessionInt(session, "userId");
        Integer roleId = getSessionInt(session, "roleId");

        if (userId == null || roleId == 2) {
            return "index";
        }

        Product product = productService.findByProductId(productId);

        ProductUpdateForm updateForm = new ProductUpdateForm();
        updateForm.setProductId(product.getProductId());
        updateForm.setProductName(product.getProductName());
        updateForm.setCategoryId(product.getCategoryId());
        updateForm.setKeywordId(product.getKeywordId());
        updateForm.setCountryId(product.getCountryId());
        updateForm.setManufactureDate(product.getManufactureDate());
        updateForm.setDescription(product.getDescription());
        updateForm.setStockS(product.getStockS() != null ? product.getStockS() : 0);
        updateForm.setStockM(product.getStockM() != null ? product.getStockM() : 0);
        updateForm.setStockL(product.getStockL() != null ? product.getStockL() : 0);
        updateForm.setStockXL(product.getStockXL() != null ? product.getStockXL() : 0);
        updateForm.setRegularPrice(product.getRegularPrice());
        updateForm.setDiscountRate(product.getDiscountRate());
        updateForm.setSalePrice(product.getSalePrice());
        updateForm.setImagePath(product.getImagePath());

        model.addAttribute("productUpdateForm", updateForm);
        model.addAttribute("categoryList", productService.findAllCategory());
        model.addAttribute("keywordList", productService.findAllKeyword());
        model.addAttribute("countryList", productService.findAllCountry());

        return "productView/prodUpdate";
    }

    /**
     * 商品情報更新処理。
     * 入力チェック後、データベースを更新。
     *
     * @param productUpdateForm 更新対象のフォームオブジェクト
     * @param productForm       商品検索フォーム（画面に再表示するための補助用）
     * @param result            バリデーション結果
     * @param model             モデルオブジェクト
     * @return 商品一覧画面テンプレート、またはエラー時は更新画面
     */
    @PostMapping("/productUpdate")
    public String productUpdate(@Validated @ModelAttribute("productUpdateForm") ProductUpdateForm productUpdateForm,
            @ModelAttribute("productForm") ProductForm productForm,
            BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        Integer userId = getSessionInt(session, "userId");
        Integer roleId = getSessionInt(session, "roleId");

        if (userId == null || roleId == 2) {
            return "index";
        }

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
        Integer discountRate = productUpdateForm.getDiscountRate() != null ? productUpdateForm.getDiscountRate() : 0;
        Integer salesPrice = (int) (regularPrice * (1 - discountRate * 0.01));

        MultipartFile image = productUpdateForm.getProductImage();
        String imagePath = (image != null && !image.isEmpty()) ? saveImage(image) : productUpdateForm.getImagePath();

        if (imagePath == null || imagePath.isEmpty()) {
            Product existing = productService.findByProductId(productId);
            imagePath = existing.getImagePath();
        }

        ProductDto productDto = new ProductDto(productId, productName, categoryId, keywordId, countryId,
                manufactureDate, regularPrice, discountRate, salesPrice,
                stockS, stockM, stockL, stockXL, imagePath, description);

        productService.updateProduct(productDto);

        model.addAttribute("productList", productService.findByAll());
        model.addAttribute("recommendList", productService.findByRecommends());

        redirectAttributes.addFlashAttribute("successUpdateProduct", "「" + productName + "」商品の情報を更新しました。");

        return "redirect:/productList";
    }

    /**
     * 指定された商品を削除する処理。
     *
     * @param productId 削除対象の商品ID
     * @param model     ビューに渡すモデル
     * @return 商品一覧画面テンプレート
     */
    @GetMapping("/productDelete")
    public String productDelete(@RequestParam("productId") Integer productId, Model model, RedirectAttributes redirectAttributes) {

        Integer userId = getSessionInt(session, "userId");
        Integer roleId = getSessionInt(session, "roleId");

        if (userId == null || roleId == 2) {
            return "index";
        }

        try {
            productService.deleteProductWithCart(productId);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("deleteError", "商品を削除できません。関連するデータが存在します。");
            return "error/customError";
        }

        ProductForm productForm = new ProductForm();
        productForm.setSearchType("productName");
        productForm.setSortOrder("created_at");

        model.addAttribute("productForm", productForm);
        model.addAttribute("productList", productService.findByAll());
        model.addAttribute("recommendList", productService.findByRecommends());

        redirectAttributes.addFlashAttribute("successDeleteProduct", "商品の情報を削除しました。");

        return "redirect:/productList";
    }

    /**
     * アップロードされた画像ファイルを保存し、保存先パスを返却する。
     *
     * @param image MultipartFile形式の画像ファイル
     * @return 保存された画像の相対パス。保存失敗時はnull。
     */
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

    private Integer getSessionInt(HttpSession session, String key) {
        Object value = session.getAttribute(key);
        if (value == null)
            return null;
        try {
            return Integer.valueOf(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

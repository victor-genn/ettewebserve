package jp.co.genproject.ettewebserve.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ホーム画面のコントローラークラス。
 * トップページ（index）の初期表示を担当する。
 *
 * 主な機能：
 * ・トップページの表示
 *
 * 使用技術：
 * ・Spring MVC
 *
 * @author 張勝現
 * @version 1.0
 */
@Controller
public class HomeController {

    @GetMapping({ "/", "/index" })
    public String index() {
        return "index";
    }
}
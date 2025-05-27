package jp.co.genproject.ettewebserve.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ホーム画面の表示を担当するコントローラークラス。
 *
 * 主な機能：
 * トップページ（index）の初期表示を行う。
 *
 * 使用技術：
 * Spring MVC を使用。
 *
 * @author 張勝現
 * @version 1.0
 */
@Controller
public class HomeController {

    @GetMapping({"/","/index"})
    public String index() {
        return "index";
    }
}

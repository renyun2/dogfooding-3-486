package com.student.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * vue-router History 模式下，浏览器直接刷新 /login 等路径时需回到 index.html，否则静态资源会去解析名为 login 的文件并报错。
 */
@Controller
public class SpaForwardController {

    @GetMapping({
            "/",
            "/login",
            "/classes",
            "/students",
            "/courses",
            "/grades"
    })
    public String forwardIndex() {
        return "forward:/index.html";
    }
}

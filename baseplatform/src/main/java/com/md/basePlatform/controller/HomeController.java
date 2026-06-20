package com.md.basePlatform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 入口重定向。
 */
@Controller
public class HomeController {

    /**
     * 根路径跳转到列表。
     *
     * @return 重定向目标
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/uavs";
    }
}

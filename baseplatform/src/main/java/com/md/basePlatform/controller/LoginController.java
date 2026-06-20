package com.md.basePlatform.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 登录 / 注销入口。
 */
@Controller
public class LoginController {

    /**
     * 登录页。
     *
     * @return 视图名
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * 提交登录表单。
     *
     * @param username 用户名
     * @param password 密码
     * @param ra       flash
     * @return 重定向
     */
    @PostMapping("/login")
    public String loginSubmit(@RequestParam("username") String username,
            @RequestParam("password") String password,
            RedirectAttributes ra) {
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(new UsernamePasswordToken(username, password));
            return "redirect:/uavs";
        } catch (AuthenticationException ex) {
            ra.addFlashAttribute("loginError", "用户名或密码错误");
            return "redirect:/login";
        }
    }
}

package com.example.lab6.Controller;

import com.example.lab6.Service.CookieService;
import com.example.lab6.Service.ParamService;
import com.example.lab6.Service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AccountController {

    @Autowired
    CookieService cookieService;
    @Autowired
    ParamService paramService;
    @Autowired
    SessionService sessionService;

    // Lưu danh sách tài khoản đăng ký tạm thời (username -> password)
    Map<String, String> accounts = new HashMap<>();

    @GetMapping("/account/login")
    public String loginForm() {
        return "/account/login";
    }

    @PostMapping("/account/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "remember", required = false) String remember) {

        boolean rememberMe = "true".equals(remember);

        // Kiểm tra tài khoản có tồn tại hay không
        if (accounts.containsKey(username) && accounts.get(username).equals(password)) {
            sessionService.set("username", username);

            if (rememberMe) {
                cookieService.add("user", username, 10 * 24); // 10 ngày
            } else {
                cookieService.remove("user");
            }

            return "redirect:/item/index";
        }

        return "/account/login";
    }

    @GetMapping("/account/register")
    public String registerForm() {
        return "/account/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam("photo") MultipartFile file,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password) {
        paramService.save(file, "/uploads");
        accounts.put(username, password);
        return "redirect:/account/login";
    }
}

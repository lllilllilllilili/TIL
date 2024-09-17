package com.example.test.account;

import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {
    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        return "account/sign-up"; //처리 방법에 대해 고민
    }
}

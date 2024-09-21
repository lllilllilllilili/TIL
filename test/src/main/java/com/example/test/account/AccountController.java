package com.example.test.account;

import ch.qos.logback.core.model.Model;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountController  implements AccountControllerDocs  {
    @GetMapping("/sign-up")
    public String signUpForm(String userId) {
        return "account/sign-up"; //처리 방법에 대해 고민
    }

    @PostMapping("sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) {
            return "account/sign-up";
        }
        return "redirect:/";
    }
}

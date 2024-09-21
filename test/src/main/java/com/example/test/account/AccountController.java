package com.example.test.account;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AccountController  implements AccountControllerDocs  {

    private final SignUpFormValidator signUpFormValidator;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }


    @GetMapping("/sign-up")
    public String signUpForm(String userId) {
        return "account/sign-up"; //처리 방법에 대해 고민
    }

    @PostMapping("sign-up")
    public String signUpSubmit(@RequestBody @Valid SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) {
            log.info("signForm not valid");
            return "account/sign-up";
        }

        //signUpFormValidator.validate(signUpForm, errors);

        log.info("signForm valid");
        return "redirect:/";
    }
}

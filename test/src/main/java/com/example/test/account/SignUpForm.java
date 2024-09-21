package com.example.test.account;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpForm {
    //검증 추가
    private String nickname;
    private String email;
    private String password;
}

package com.example.test.account;

import ch.qos.logback.core.model.Model;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Test", description = "Test 관련 API입니다.")
public interface AccountControllerDocs {
    @Operation(summary = "유저 정보 저장", description = "유저 정보를 저장합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유저 정보 저장 성공"),
        @ApiResponse(responseCode = "409", description = "유저 정보 저장 실패(유저 중복)") })
    String signUpForm(SignUpForm signUpForm);
}

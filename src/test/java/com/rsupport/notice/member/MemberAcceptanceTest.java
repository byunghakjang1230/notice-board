package com.rsupport.notice.member;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.rsupport.notice.auth.dto.LoginRequest;
import com.rsupport.notice.auth.dto.LoginResponse;
import com.rsupport.notice.member.dto.MemberRequest;
import com.rsupport.notice.member.dto.MemberResponse;
import com.rsupport.notice.utils.AcceptanceTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("회원 가입/로그인 통합 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemberAcceptanceTest extends AcceptanceTest {
    private static final String DEFAULT_URL = "/api/members";
    private static final String LOGIN_URL = "/api/login";
    public static final String USER_EMAIL = "user@email.com";

    @Test
    @Order(1)
    @DisplayName("Email로 회원가입을 할 수 있다.")
    void member_join() {
        // given
        MemberRequest memberRequest = new MemberRequest("user@email.com", "1234");

        // when
        ExtractableResponse<Response> 회원가입_결과 = 회원가입_요청(memberRequest);

        // then
        회원가입_요청_확인(회원가입_결과);
    }

    @Test
    @Order(2)
    @DisplayName("회원 로그인을 할 수 있다.")
    void member_login() {
        // given
        MemberResponse savedMemberResponse = 회원이_등록되어_있음(new MemberRequest("user@email.com", "1234"));

        // when
        ExtractableResponse<Response> 로그인_결과 = 회원_로그인_요청(new LoginRequest("user@email.com", "1234"));

        회원_로그인_요청_확인(로그인_결과);
    }

    public static String 회원_로그인_되어있음_공개(String email, String password){
        return RestAssured
                .given().log().all()
                .when()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LoginRequest(email, password))
                .post(LOGIN_URL)
                .then().log().all()
                .extract().as(LoginResponse.class).getToken();
    }

    private void 회원_로그인_요청_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 회원_로그인_요청(LoginRequest loginRequest) {
        return RestAssured
                .given().log().all()
                .when()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(loginRequest)
                .post(LOGIN_URL)
                .then().log().all()
                .extract();
    }

    public static MemberResponse 회원이_등록되어_있음_공개(String email, String password){
        return RestAssured
                .given().log().all()
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(new MemberRequest(email, password))
                .post(DEFAULT_URL)
                .then().log().all()
                .extract().as(MemberResponse.class);
    }

    private MemberResponse 회원이_등록되어_있음(MemberRequest memberRequest) {
        return 회원가입_요청(memberRequest).as(MemberResponse.class);
    }

    private ExtractableResponse<Response> 회원가입_요청(MemberRequest memberRequest) {
        return RestAssured
                .given().log().all()
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .post(DEFAULT_URL)
                .then().log().all()
                .extract();
    }

    private void 회원가입_요청_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}

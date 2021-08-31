package com.rsupport.notice.member;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.rsupport.notice.auth.dto.LoginRequest;
import com.rsupport.notice.auth.dto.LoginResponse;
import com.rsupport.notice.common.dto.ErrorResponse;
import com.rsupport.notice.member.dto.MemberRequest;
import com.rsupport.notice.member.dto.MemberResponse;
import com.rsupport.notice.utils.AcceptanceTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("회원 가입/로그인 통합 테스트")
public class MemberAcceptanceTest extends AcceptanceTest {
    private static final String MEMBER_URL = "/api/members";
    private static final String LOGIN_URL = "/api/login";
    public static final String USER_EMAIL_A = "user@email.com";
    public static final String USER_EMAIL_B = "user1@email.com";

    @Test
    @DisplayName("Email로 회원가입을 할 수 있다.")
    void member_join() {
        // given
        MemberRequest memberRequest = new MemberRequest(USER_EMAIL_A, "1234");

        // when
        ExtractableResponse<Response> 회원가입_결과 = 회원가입_요청(memberRequest);

        // then
        회원가입_요청_확인(회원가입_결과);
    }

    @Test
    @DisplayName("회원 로그인을 할 수 있다.")
    void member_login() {
        // given
        회원이_등록되어_있음(new MemberRequest(USER_EMAIL_A, "1234"));

        // when
        ExtractableResponse<Response> 로그인_결과 = 회원_로그인_요청(new LoginRequest(USER_EMAIL_A, "1234"));

        회원_로그인_요청_확인(로그인_결과);
    }

    @Test
    @DisplayName("중복 이메일 저장 오류 발생")
    void duplicate_email_exception() {
        회원이_등록되어_있음(new MemberRequest(USER_EMAIL_A, "1234"));

        // when
        ExtractableResponse<Response> 회원가입_결과 = 회원가입_요청(new MemberRequest(USER_EMAIL_A, "1234"));

        // then
        회원가입_실패_확인(회원가입_결과);
        요청실패_메시지_확인(회원가입_결과.as(ErrorResponse.class), "이미 사용중인 이메일입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인 시도 시 오류 발생")
    void login_fail_notExistsEmail_exception() {
        // given
        회원이_등록되어_있음(new MemberRequest(USER_EMAIL_A, "1234"));

        // when
        ExtractableResponse<Response> 로그인_결과 = 회원_로그인_요청(new LoginRequest(USER_EMAIL_B, "1234"));

        회원_로그인_실패_확인(로그인_결과);
        요청실패_메시지_확인(로그인_결과.as(ErrorResponse.class), "조회된 사용자가 없습니다.");
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 시도 시 오류 발생")
    void login_fail_notEqualPassword_exception() {
        // given
        회원이_등록되어_있음(new MemberRequest(USER_EMAIL_A, "1234"));

        // when
        ExtractableResponse<Response> 로그인_결과 = 회원_로그인_요청(new LoginRequest(USER_EMAIL_A, "1235"));

        회원_로그인_실패_확인(로그인_결과);
        요청실패_메시지_확인(로그인_결과.as(ErrorResponse.class), "비밀변호가 맞지 않습니다.");
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
                .post(MEMBER_URL)
                .then().log().all()
                .extract().as(MemberResponse.class);
    }

    private ExtractableResponse<Response> 회원가입_요청(MemberRequest memberRequest) {
        return RestAssured
                .given().log().all()
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .post(MEMBER_URL)
                .then().log().all()
                .extract();
    }

    private MemberResponse 회원이_등록되어_있음(MemberRequest memberRequest) {
        return 회원가입_요청(memberRequest).as(MemberResponse.class);
    }

    private void 회원가입_요청_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 회원_로그인_요청_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 회원_로그인_실패_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 회원가입_실패_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 요청실패_메시지_확인(ErrorResponse errorResponse, String errorMessage) {
        assertThat(errorResponse.getErrorMessage()).isEqualTo(errorMessage);
    }
}

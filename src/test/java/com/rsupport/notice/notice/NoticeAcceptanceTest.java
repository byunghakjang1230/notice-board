package com.rsupport.notice.notice;

import static com.rsupport.notice.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.rsupport.notice.common.dto.ErrorResponse;
import com.rsupport.notice.notice.dto.NoticeRequest;
import com.rsupport.notice.notice.dto.NoticeResponse;
import com.rsupport.notice.utils.AcceptanceTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("공지사항 등록/수정/조회 통합 테스트")
class NoticeAcceptanceTest extends AcceptanceTest {
    private static final String DEFAULT_URL = "/api/notices";
    private String loginToken;
    private String loginToken1;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        회원이_등록되어_있음_공개(USER_EMAIL_A, "123");
        회원이_등록되어_있음_공개(USER_EMAIL_B, "123");
        loginToken = "Bearer " + 회원_로그인_되어있음_공개(USER_EMAIL_A, "123");
        loginToken1 = "Bearer " + 회원_로그인_되어있음_공개(USER_EMAIL_B, "123");
        System.out.println(loginToken);
        System.out.println(loginToken1);
        공지사항이_등록되어_있음(new NoticeRequest("제목1", "내용1"));
        공지사항이_등록되어_있음(new NoticeRequest("제목2", "내용2"));
        공지사항이_등록되어_있음(new NoticeRequest("제목3", "내용3"));
        공지사항이_등록되어_있음(new NoticeRequest("제목4", "내용4"));
        공지사항이_등록되어_있음(new NoticeRequest("제목5", "내용5"));
        공지사항이_등록되어_있음(new NoticeRequest("제목6", "내용6"));
        공지사항이_등록되어_있음(new NoticeRequest("제목7", "내용7"));
    }

    @Test
    @DisplayName("공지사항을 등록한다.")
    void save_notice() {
        // given
        NoticeRequest noticeRequest = new NoticeRequest("제목", "내용");

        // when
        ExtractableResponse<Response> 공지사항_등록_결과 = 공지사항_등록_요청(noticeRequest);

        // then
        공지사항_등록_요청_성공_확인(공지사항_등록_결과);
    }

    @Test
    @DisplayName("등록된 공지사항을 조회한다.")
    void find_notice() {
        // given
        NoticeResponse noticeResponse = 공지사항이_등록되어_있음(new NoticeRequest("제목", "내용"));

        // when
        ExtractableResponse<Response> 공지사항_조회_결과 = 공지사항_조회_요청(noticeResponse.getId());

        // then
        공지사항_조회_요청_성공_확인(공지사항_조회_결과);
    }

    @Test
    @DisplayName("등록된 공지사항을 수정한다.")
    void modify_notice() {
        // given
        NoticeResponse registeredNotice = 공지사항이_등록되어_있음(new NoticeRequest("제목", "내용"));
        NoticeRequest noticeModifyRequest = new NoticeRequest("제목1", "내용1");

        // when
        ExtractableResponse<Response> 공지사항_수정_결과 = 공지사항_수정_요청(registeredNotice.getId(), noticeModifyRequest, loginToken);

        // then
        공지사항_수정_요청_성공_확인(공지사항_수정_결과);
        공지사항_수정_내용_확인(공지사항_수정_결과.as(NoticeResponse.class), noticeModifyRequest);
    }

    @Test
    @DisplayName("등록된 공지사항을 삭제한다.")
    void delete_notice() {
        // given
        NoticeResponse registeredNotice = 공지사항이_등록되어_있음(new NoticeRequest("제목", "내용"));
        NoticeRequest noticeModifyRequest = new NoticeRequest("제목", "내용");

        // when
        ExtractableResponse<Response> 공지사항_삭제_결과 = 공지사항_삭제_요청(registeredNotice.getId(), noticeModifyRequest, loginToken);

        // then
        공지사항_삭제_요청_성공_확인(공지사항_삭제_결과);
    }

    @Test
    @DisplayName("등록된 공지사항 목록을 조회한다.")
    void find_all_notice_with_paging() {
        // given
        int page = 0;
        int size = 3;

        // when
        ExtractableResponse<Response> 공지사항_목록_조회_결과 = 공지사항_목록_조회_요청(page, size);

        // then
        공지사항_목록_조회_요청_성공_확인(공지사항_목록_조회_결과);
    }

    @Test
    @DisplayName("공지사항 조회 실패 오류발생")
    void find_notice_notFount_exception() {
        // when
        ExtractableResponse<Response> 공지사항_조회_결과 = 공지사항_조회_요청(100L);

        // then
        공지사항_조회_요청_실패_확인(공지사항_조회_결과);
        요청실패_메시지_확인(공지사항_조회_결과.as(ErrorResponse.class), "요청한 공지사항이 없습니다.");
    }

    @Test
    @DisplayName("공지사항 수정 권한 없음으로 실패 오류 발생")
    void update_notice_fail_exception() {
        // given
        NoticeResponse registeredNotice = 공지사항이_등록되어_있음(new NoticeRequest("제목", "내용"));
        NoticeRequest noticeModifyRequest = new NoticeRequest("제목1", "내용1");

        // when
        ExtractableResponse<Response> 공지사항_수정_결과 = 공지사항_수정_요청(registeredNotice.getId(), noticeModifyRequest, loginToken1);

        // then
        공지사항_수정_요청_실패_확인(공지사항_수정_결과);
        요청실패_메시지_확인(공지사항_수정_결과.as(ErrorResponse.class), "공지사항에 대한 변경 권한이 없습니다.");
    }

    @Test
    @DisplayName("공지사항 삭제 권한 없음으로 실패 오류 발생")
    void delete_notice_fail_exception() {
        // given
        NoticeResponse registeredNotice = 공지사항이_등록되어_있음(new NoticeRequest("제목", "내용"));
        NoticeRequest noticeModifyRequest = new NoticeRequest("제목", "내용");

        // when
        ExtractableResponse<Response> 공지사항_삭제_결과 = 공지사항_삭제_요청(registeredNotice.getId(), noticeModifyRequest, loginToken1);

        // then
        공지사항_삭제_요청_실패_확인(공지사항_삭제_결과);
        요청실패_메시지_확인(공지사항_삭제_결과.as(ErrorResponse.class), "공지사항에 대한 변경 권한이 없습니다.");
    }

    private void 공지사항_삭제_요청_실패_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 공지사항_수정_요청_실패_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 요청실패_메시지_확인(ErrorResponse errorResponse, String errorMessage) {
        assertThat(errorResponse.getErrorMessage()).isEqualTo(errorMessage);
    }

    private void 공지사항_조회_요청_실패_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 공지사항_목록_조회_요청(int page, int size) {
        return RestAssured
                .given().log().all()
                .when()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .get(DEFAULT_URL + "?page=" + page + "&size=" + size)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 공지사항_삭제_요청(Long id, NoticeRequest noticeRequest, String loginToken) {
        return RestAssured
                .given().log().all()
                .when()
                .header("Authorization", loginToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(noticeRequest)
                .delete(DEFAULT_URL + "/" + id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 공지사항_등록_요청(NoticeRequest noticeRequest) {
        return RestAssured
                .given().log().all()
                .when()
                .header("Authorization", loginToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(noticeRequest)
                .post(DEFAULT_URL)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 공지사항_조회_요청(Long id) {
        return RestAssured
                .given().log().all()
                .when()
                .header("Authorization", loginToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .get(DEFAULT_URL + "/" + id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 공지사항_수정_요청(Long id, NoticeRequest noticeRequest, String loginToken) {
        return RestAssured
                .given().log().all()
                .when()
                .header("Authorization", loginToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(noticeRequest)
                .put(DEFAULT_URL + "/" + id)
                .then().log().all()
                .extract();
    }

    private NoticeResponse 공지사항이_등록되어_있음(NoticeRequest noticeRequest) {
        return 공지사항_등록_요청(noticeRequest).as(NoticeResponse.class);
    }

    private void 공지사항_등록_요청_성공_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 공지사항_조회_요청_성공_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 공지사항_수정_내용_확인(NoticeResponse resultNoticeResponse, NoticeRequest noticeModifyRequest) {
        assertAll(
                () -> assertThat(resultNoticeResponse.getTitle()).isEqualTo(noticeModifyRequest.getTitle()),
                () -> assertThat(resultNoticeResponse.getContent()).isEqualTo(noticeModifyRequest.getContent())
        );
    }

    private void 공지사항_수정_요청_성공_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 공지사항_삭제_요청_성공_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 공지사항_목록_조회_요청_성공_확인(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}

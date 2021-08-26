package com.rsupport.notice;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.rsupport.notice.dto.NoticeRequest;
import com.rsupport.notice.dto.NoticeResponse;
import com.rsupport.notice.utils.AcceptanceTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("공지사항 등록/수정/조회 통합 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NoticeAcceptanceTest extends AcceptanceTest {

    private static final String DEFAULT_URL = "/api/notices";

    @Test
    @Order(1)
    @DisplayName("공지사항을 등록한다.")
    void save_notice() {
        // given
        NoticeRequest noticeRequest = new NoticeRequest("제목", "내용", "user@email.com");

        // when
        ExtractableResponse<Response> 공지사항_등록_결과 = 공지사항_등록_요청(noticeRequest);

        // then
        공지사항_등록_요청_성공_확인(공지사항_등록_결과);
    }

    @Test
    @DisplayName("등록된 공지사항을 조회한다.")
    void find_notice() {
        // given
        NoticeResponse noticeResponse = 공지사항이_등록되어_있음(new NoticeRequest("제목", "내용", "user@email.com"));

        // when
        ExtractableResponse<Response> 공지사항_조회_결과 = 공지사항_조회_요청(noticeResponse.getId());

        // then
        공지사항_조회_요청_성공_확인(공지사항_조회_결과);
    }

    private ExtractableResponse<Response> 공지사항_등록_요청(NoticeRequest noticeRequest) {
        return RestAssured
                .given().log().all()
                .when()
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
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .get(DEFAULT_URL + "/" + id)
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
}

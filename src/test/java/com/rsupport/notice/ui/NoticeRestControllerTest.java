package com.rsupport.notice.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsupport.notice.service.NoticeService;
import com.rsupport.notice.dto.NoticeRequest;
import com.rsupport.notice.dto.NoticeResponse;
import com.rsupport.notice.utils.MockMvcControllerTest;

@WebMvcTest(controllers = NoticeRestController.class)
@DisplayName("NoticeRestController 단위 테스트")
class NoticeRestControllerTest extends MockMvcControllerTest {
    private static final String DEFAULT_REQUEST_URL = "/api/notices";

    @Autowired
    private NoticeRestController noticeRestController;
    @MockBean
    private NoticeService noticeService;

    @Override
    protected Object controller() {
        return this.noticeRestController;
    }

    @Test
    @DisplayName("POST 등록 요청 정상 등록 확인")
    void post_save_notice() throws Exception {
        // given
        NoticeRequest noticeRequest = new NoticeRequest("제목", "내용", "user@email.com");
        NoticeResponse noticeResponse = NoticeResponse.of(1L, noticeRequest.getTitle(), noticeRequest.getContent(), noticeRequest.getWriter());
        given(this.noticeService.saveNotice(any(NoticeRequest.class))).willReturn(noticeResponse);

        // when - then
        this.mockMvc.perform(post(DEFAULT_REQUEST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(noticeRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
        ;
    }

    @Test
    @DisplayName("GET 조회 요청 정상 조회 확인")
    void get_find_notice() throws Exception {
        // given
        NoticeResponse noticeResponse = NoticeResponse.of(1L, "제목", "내용", "user@email.com");
        given(this.noticeService.findNoticeBy(1L)).willReturn(noticeResponse);

        // when - then
        this.mockMvc.perform(get(DEFAULT_REQUEST_URL + "/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
        ;
    }

    @Test
    @DisplayName("PUT 수정 요청 정상 완료 확인")
    void put_update_notice() throws Exception {
        // given
        NoticeRequest noticeRequest = new NoticeRequest("제목1", "내용1", "user@email.com");
        given(noticeService.updateNotice(anyLong(), any(NoticeRequest.class))).willReturn(NoticeResponse.of(1L, "제목1", "내용1", "user@email.com"));

        // when - then
        this.mockMvc.perform(put(DEFAULT_REQUEST_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(noticeRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value(noticeRequest.getTitle()))
                .andExpect(jsonPath("content").value(noticeRequest.getContent()))
        ;
    }

    @Test
    @DisplayName("DELETE 삭제 요청 정상 완료 확인")
    void delete_remove_notice() throws Exception {
        // given
        NoticeRequest noticeRequest = new NoticeRequest("제목", "내용", "user@email.com");

        // when - then
        this.mockMvc.perform(delete(DEFAULT_REQUEST_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(noticeRequest)))
                .andDo(print())
                .andExpect(status().isNoContent())
        ;
        verify(noticeService).deleteNotice(anyLong(), any(NoticeRequest.class));
    }
}

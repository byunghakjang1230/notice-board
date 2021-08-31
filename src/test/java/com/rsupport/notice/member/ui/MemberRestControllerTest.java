package com.rsupport.notice.member.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.rsupport.notice.member.domain.Member;
import com.rsupport.notice.member.dto.MemberRequest;
import com.rsupport.notice.member.dto.MemberResponse;
import com.rsupport.notice.member.service.MemberService;
import com.rsupport.notice.utils.MockMvcControllerTest;

@WebMvcTest(controllers = MemberRestController.class)
@DisplayName("MemberRestController 단위 테스트")
class MemberRestControllerTest extends MockMvcControllerTest {
    private static final String DEFAULT_REQUEST_URL = "/api/members";
    @Autowired
    private MemberRestController memberRestController;
    @MockBean
    private MemberService memberService;

    @Override
    protected Object controller() {
        return this.memberRestController;
    }

    @Test
    @DisplayName("사용자 회원가입")
    void join_member() throws Exception {
        MemberRequest memberRequest = new MemberRequest("user@email.com", "123");
        MemberResponse memberResponse = MemberResponse.of(new Member(memberRequest.getEmail(), memberRequest.getPassword()));
        given(memberService.joinMember(any(MemberRequest.class))).willReturn(memberResponse);

        // when - then
        this.mockMvc.perform(post(DEFAULT_REQUEST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(memberRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value(memberRequest.getEmail()))
        ;
    }
}

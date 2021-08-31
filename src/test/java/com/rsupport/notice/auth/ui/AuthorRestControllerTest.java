package com.rsupport.notice.auth.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsupport.notice.auth.dto.LoginRequest;
import com.rsupport.notice.auth.dto.LoginResponse;
import com.rsupport.notice.auth.service.AuthService;
import com.rsupport.notice.utils.MockMvcControllerTest;

@DisplayName("AuthorRestController 단위 테스트")
@WebMvcTest(controllers = AuthorRestController.class)
class AuthorRestControllerTest extends MockMvcControllerTest {
    private static final String DEFAULT_REQUEST_URL = "/api/login";
    @Autowired
    private AuthorRestController authorRestController;

    @Override
    protected Object controller() {
        return this.authorRestController;
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        LoginRequest loginRequest = new LoginRequest("user@email.com", "1234");
        LoginResponse loginResponse = new LoginResponse("123123123");
        given(authService.login(any(LoginRequest.class))).willReturn(loginResponse);

        // when - then
        this.mockMvc.perform(post(DEFAULT_REQUEST_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("token").value(loginResponse.getToken()))
        ;
    }
}

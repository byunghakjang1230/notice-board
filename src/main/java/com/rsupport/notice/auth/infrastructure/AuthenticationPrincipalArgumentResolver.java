package com.rsupport.notice.auth.infrastructure;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.rsupport.notice.auth.domain.AuthenticationPrincipal;
import com.rsupport.notice.auth.domain.LoginUser;
import com.rsupport.notice.auth.service.AuthService;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthService authService;

    public AuthenticationPrincipalArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) {
        return JwtTokenExtractor.extractToken(webRequest.getNativeRequest(HttpServletRequest.class))
                .map(authService::findMemberByToken)
                .orElse(new LoginUser());
    }
}

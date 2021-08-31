package com.rsupport.notice.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rsupport.notice.auth.exception.AuthorizationException;
import com.rsupport.notice.common.dto.ErrorResponse;
import com.rsupport.notice.member.exception.EmailExistsException;
import com.rsupport.notice.member.exception.MemberNotFoundException;
import com.rsupport.notice.notice.exception.NoticeNotFoundException;
import com.rsupport.notice.notice.exception.NoticePermissionDeniedException;

@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<ErrorResponse> handlingEmailExistsException(EmailExistsException exception) {
        return ResponseEntity.badRequest().body(ErrorResponse.of(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlingMemberNotFoundException(MemberNotFoundException exception) {
        return ResponseEntity.badRequest().body(ErrorResponse.of(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler(NoticeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlingNoticeNotFoundException(NoticeNotFoundException exception) {
        return ResponseEntity.badRequest().body(ErrorResponse.of(HttpStatus.UNAUTHORIZED, exception.getMessage()));
    }

    @ExceptionHandler(NoticePermissionDeniedException.class)
    public ResponseEntity<ErrorResponse> handlingNoticePermissionDeniedException(NoticePermissionDeniedException exception) {
        return ResponseEntity.badRequest().body(ErrorResponse.of(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @CrossOrigin("*")
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handlingAuthorizationException(AuthorizationException exception) {
        return ResponseEntity.badRequest().body(ErrorResponse.of(HttpStatus.UNAUTHORIZED, exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handlingException(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity.badRequest().body(ErrorResponse.of(HttpStatus.BAD_REQUEST));
    }
}

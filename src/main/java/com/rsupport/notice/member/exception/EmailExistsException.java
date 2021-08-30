package com.rsupport.notice.member.exception;

public class EmailExistsException extends RuntimeException {
    public EmailExistsException() {
        super("사용중인 이메일입니다.");
    }

    public EmailExistsException(String message) {
        super(message);
    }
}

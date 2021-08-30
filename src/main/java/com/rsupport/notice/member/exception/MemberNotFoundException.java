package com.rsupport.notice.member.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException() {
        super("조회된 사용자가 없습니다.");
    }

    public MemberNotFoundException(String message) {
        super(message);
    }
}

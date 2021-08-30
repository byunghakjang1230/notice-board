package com.rsupport.notice.notice.exception;

public class NoticeNotFoundException extends RuntimeException {
    public NoticeNotFoundException() {
        super("조회된 공지사항이 없습니다.");
    }

    public NoticeNotFoundException(String message) {
        super(message);
    }
}

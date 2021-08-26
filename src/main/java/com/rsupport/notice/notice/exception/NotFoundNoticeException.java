package com.rsupport.notice.notice.exception;

public class NotFoundNoticeException extends RuntimeException {
    public NotFoundNoticeException() {
        super("조회된 공지사항이 없습니다.");
    }

    public NotFoundNoticeException(String message) {
        super(message);
    }
}

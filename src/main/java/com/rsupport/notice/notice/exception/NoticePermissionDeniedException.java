package com.rsupport.notice.notice.exception;

public class NoticePermissionDeniedException extends RuntimeException {
    public NoticePermissionDeniedException() {
        super("공지사항에 접근 권한이 없습니다.");
    }

    public NoticePermissionDeniedException(String message) {
        super(message);
    }
}

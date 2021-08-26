package com.rsupport.notice.notice.dto;

import com.rsupport.notice.notice.domain.Notice;

public class NoticeRequest {
    private String title;
    private String content;
    private String writer;

    protected NoticeRequest() {
    }

    public NoticeRequest(String title, String content, String writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getWriter() {
        return writer;
    }

    public Notice toNotice() {
        return new Notice(this.title, this.content, this.writer);
    }
}

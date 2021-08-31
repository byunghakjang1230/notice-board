package com.rsupport.notice.notice.dto;

import com.rsupport.notice.notice.domain.Notice;

public class NoticeRequest {
    private String title;
    private String content;

    protected NoticeRequest() {
    }

    public NoticeRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Notice toUpdateNotice() {
        return Notice.createForUpdate(this.title, this.content);
    }
}

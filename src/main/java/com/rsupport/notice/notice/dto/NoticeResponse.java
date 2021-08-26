package com.rsupport.notice.notice.dto;

import java.time.LocalDate;

import com.rsupport.notice.notice.domain.Notice;

public class NoticeResponse {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private LocalDate createDate;
    private LocalDate lastModifiedDate;

    protected NoticeResponse() {
    }

    private NoticeResponse(Long id, String title, String content, String writer, LocalDate createDate, LocalDate lastModifiedDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.createDate = createDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public static NoticeResponse of(Long id, String title, String content, String writer) {
        return new NoticeResponse(id, title, content, writer, null, null);
    }

    public static NoticeResponse of(Notice notice) {
        return new NoticeResponse(notice.getId(), notice.getTitle(), notice.getContent(), notice.getWriter(), notice.getCreateDate(), notice.getLastModifiedDate());
    }

    public Long getId() {
        return id;
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

    public LocalDate getCreateDate() {
        return createDate;
    }

    public LocalDate getLastModifiedDate() {
        return lastModifiedDate;
    }
}

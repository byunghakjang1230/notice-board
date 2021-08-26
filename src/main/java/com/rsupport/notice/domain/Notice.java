package com.rsupport.notice.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Lob
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private String writer;
    @Column(name = "create_date_time")
    private LocalDateTime createDateTime;
    @Column(name = "last_modified_date_time")
    private LocalDateTime lastModifiedDateTime;
    @Column(length = 5)
    private boolean deleted;

    protected Notice() {
    }

    public Notice(String title, String content, String writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.createDateTime = LocalDateTime.now();
        this.lastModifiedDateTime = LocalDateTime.now();
        this.deleted = false;
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
        return this.createDateTime.toLocalDate();
    }

    public LocalDate getLastModifiedDate() {
        return this.lastModifiedDateTime.toLocalDate();
    }

    public void deleted() {
        this.deleted = true;
    }

    public boolean hasSameOwner(String writer) {
        return this.writer.equals(writer);
    }

    public void updateTo(Notice updateNotice) {
        this.title = updateNotice.title;
        this.content = updateNotice.content;
        this.lastModifiedDateTime = LocalDateTime.now();
    }
}

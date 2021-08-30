package com.rsupport.notice.notice.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.*;

import com.rsupport.notice.member.domain.Member;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private Member writer;
    @Column(name = "create_date_time")
    private LocalDateTime createDateTime;
    @Column(name = "last_modified_date_time")
    private LocalDateTime lastModifiedDateTime;
    @Column(length = 5)
    private boolean deleted;

    protected Notice() {
    }

    private Notice(String title, String content) {
        validateTitleIsNullOrEmpty(title);
        validateContentIsNullOrEmpty(content);
        this.title = title;
        this.content = content;
    }

    public Notice(String title, String content, Member writer) {
        validateTitleIsNullOrEmpty(title);
        validateContentIsNullOrEmpty(content);
        validateWriterIsNull(writer);
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.createDateTime = LocalDateTime.now();
        this.lastModifiedDateTime = LocalDateTime.now();
        this.deleted = false;
    }

    public static Notice createForUpdate(String title, String content) {
        return new Notice(title, content);
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

    public Member getWriter() {
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

    public boolean hasSameOwner(Member writer) {
        return this.writer.equals(writer);
    }

    public void updateTo(Notice updateNotice) {
        this.title = updateNotice.title;
        this.content = updateNotice.content;
        this.lastModifiedDateTime = LocalDateTime.now();
    }

    private void validateTitleIsNullOrEmpty(String title) {
        if (Objects.isNull(title) || title.isEmpty()) {
            throw new IllegalArgumentException("Title이 Null 또는 Empty입니다.");
        }
    }

    private void validateContentIsNullOrEmpty(String content) {
        if (Objects.isNull(content) || content.isEmpty()) {
            throw new IllegalArgumentException("Content가 Null 또는 Empty입니다.");
        }
    }

    private void validateWriterIsNull(Member writer) {
        if (Objects.isNull(writer)) {
            throw new IllegalArgumentException("Writer가 Null입니다.");
        }
    }
}

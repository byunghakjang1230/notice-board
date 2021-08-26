package com.rsupport.notice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rsupport.notice.domain.Notice;
import com.rsupport.notice.domain.NoticeRepository;
import com.rsupport.notice.dto.NoticeRequest;
import com.rsupport.notice.dto.NoticeResponse;
import com.rsupport.notice.exception.NotFoundNoticeException;

@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {
    @Mock
    private NoticeRepository noticeRepository;

    @InjectMocks
    private NoticeService noticeService;

    @Test
    @DisplayName("공지사항 등록 서비스")
    void notice_register() {
        // given
        NoticeRequest noticeRequest = new NoticeRequest("제목", "내용", "user@email.com");
        given(this.noticeRepository.save(any(Notice.class)))
                .willReturn(new Notice("제목", "내용", "user@email.com"));

        // when
        NoticeResponse noticeResponse = noticeService.saveNotice(noticeRequest);

        // then
        assertAll(
                () -> assertThat(noticeResponse).isNotNull(),
                () -> assertThat(noticeResponse.getTitle()).isEqualTo(noticeRequest.getTitle()),
                () -> assertThat(noticeResponse.getContent()).isEqualTo(noticeRequest.getContent())
        );
    }

    @Test
    @DisplayName("공지사항 단건 조회 서비스")
    void notice_findBy_id() {
        // given
        Notice notice = new Notice("제목", "내용", "user@email.com");
        given(noticeRepository.findByIdAndDeletedIsFalse(anyLong()))
                .willReturn(Optional.of(notice));

        // when
        NoticeResponse findNotice = this.noticeService.findNoticeBy(1L);

        // then
        assertAll(
                () -> assertThat(findNotice).isNotNull(),
                () -> assertThat(findNotice.getTitle()).isEqualTo(notice.getTitle())
        );
    }

    @Test
    @DisplayName("공지사항 단건 조회 실패 예외처리")
    void notice_find_exception() {
        // given
        Notice notice = new Notice("제목", "내용", "user@email.com");
        given(noticeRepository.findByIdAndDeletedIsFalse(anyLong()))
                .willReturn(Optional.empty());

        // when - then
        assertThatThrownBy(() -> this.noticeService.findNoticeBy(1L))
                .isInstanceOf(NotFoundNoticeException.class)
                .hasMessage("요청한 공지사항이 없습니다.");
    }
}

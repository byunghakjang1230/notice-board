package com.rsupport.notice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.rsupport.notice.domain.Notice;
import com.rsupport.notice.domain.NoticeRepository;
import com.rsupport.notice.dto.NoticeRequest;
import com.rsupport.notice.dto.NoticeResponse;
import com.rsupport.notice.exception.NotFoundNoticeException;
import com.rsupport.notice.exception.NoticePermissionDeniedException;

@ExtendWith(MockitoExtension.class)
@DisplayName("NoticeService 단위 테스트")
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

    @Test
    @DisplayName("공지사항 수정 서비스")
    void notice_update_by_id() {
        // given
        NoticeRequest noticeRequest = new NoticeRequest("제목1", "내용1", "user@email.com");
        given(noticeRepository.findByIdAndDeletedIsFalse(anyLong())).willReturn(Optional.of(new Notice("제목", "내용", "user@email.com")));

        // when
        NoticeResponse noticeResponse = this.noticeService.updateNotice(1L, noticeRequest);

        // then
        assertAll(
                () -> assertThat(noticeResponse).isNotNull(),
                () -> assertThat(noticeResponse.getTitle()).isEqualTo(noticeRequest.getTitle()),
                () -> assertThat(noticeResponse.getContent()).isEqualTo(noticeRequest.getContent())
        );
    }

    @Test
    @DisplayName("공지사항 수정 예외발생")
    void notice_update_with_exception() {
        // given
        NoticeRequest noticeRequest = new NoticeRequest("제목1", "내용1", "otherUser@email.com");
        given(noticeRepository.findByIdAndDeletedIsFalse(anyLong())).willReturn(Optional.of(new Notice("제목", "내용", "user@email.com")));

        // when - then
        assertThatThrownBy(() -> this.noticeService.updateNotice(1L, noticeRequest))
                .isInstanceOf(NoticePermissionDeniedException.class)
                .hasMessage("공지사항 수정 권한이 없습니다.");
    }

    @Test
    @DisplayName("공지사항 삭제 서비스")
    void notice_remove() {
        // given
        NoticeRequest noticeRequest = new NoticeRequest("제목", "내용", "user@email.com");
        given(this.noticeRepository.findByIdAndDeletedIsFalse(anyLong())).willReturn(Optional.of(new Notice("제목", "내용", "user@email.com")));

        // when
        this.noticeService.deleteNotice(1L, noticeRequest);

        // then
        verify(this.noticeRepository).delete(any(Notice.class));
    }

    @Test
    void notice_findAll_with_paging() {
        // given
        Pageable pageable = PageRequest.of(0, 3);
        Page<Notice> notices = new PageImpl<>(Arrays.asList(new Notice("제목", "내용", "user@email.com")));
        given(this.noticeRepository.findAllByDeletedIsFalse(pageable)).willReturn(notices);

        // when
        Page<NoticeResponse> noticeResponsesWithPaging = this.noticeService.findAllNoticesWithPaging(pageable);

        // then
        assertThat(noticeResponsesWithPaging.getContent()).size().isOne();
    }
}

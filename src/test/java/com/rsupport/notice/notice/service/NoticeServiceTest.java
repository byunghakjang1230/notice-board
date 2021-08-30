package com.rsupport.notice.notice.service;

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

import com.rsupport.notice.auth.domain.LoginUser;
import com.rsupport.notice.member.domain.Member;
import com.rsupport.notice.member.domain.MemberRepository;
import com.rsupport.notice.notice.domain.Notice;
import com.rsupport.notice.notice.domain.NoticeRepository;
import com.rsupport.notice.notice.dto.NoticeRequest;
import com.rsupport.notice.notice.dto.NoticeResponse;
import com.rsupport.notice.notice.exception.NoticeNotFoundException;
import com.rsupport.notice.notice.exception.NoticePermissionDeniedException;


@ExtendWith(MockitoExtension.class)
@DisplayName("NoticeService 단위 테스트")
class NoticeServiceTest {
    @Mock
    private NoticeRepository noticeRepository;
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private NoticeService noticeService;

    @Test
    @DisplayName("공지사항 등록 서비스")
    void notice_register() {
        // given
        NoticeRequest noticeRequest = new NoticeRequest("제목", "내용");
        Member member = new Member("user@email.com", "123");
        given(this.memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
        given(this.noticeRepository.save(any(Notice.class)))
                .willReturn(new Notice("제목", "내용", member));

        // when
        NoticeResponse noticeResponse = noticeService.saveNotice(noticeRequest, new LoginUser(1L, "user@email.com"));

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
        Member member = new Member("user@email.com", "123");
        Notice notice = new Notice("제목", "내용", member);
        given(noticeRepository.findByIdAndDeletedIsFalse(anyLong()))
                .willReturn(Optional.of(notice));
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));

        // when
        NoticeResponse findNotice = this.noticeService.findNoticeBy(1L, new LoginUser(1L, "user@email.com"));

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
        Member member = new Member("user@email.com", "123");
        given(noticeRepository.findByIdAndDeletedIsFalse(anyLong()))
                .willReturn(Optional.empty());
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));

        // when - then
        assertThatThrownBy(() -> this.noticeService.findNoticeBy(1L, new LoginUser(1L, "user@email.com")))
                .isInstanceOf(NoticeNotFoundException.class)
                .hasMessage("요청한 공지사항이 없습니다.");
    }

    @Test
    @DisplayName("공지사항 수정 서비스")
    void notice_update_by_id() {
        // given
        NoticeRequest noticeRequest = new NoticeRequest("제목1", "내용1");
        LoginUser loginUser = new LoginUser(1L, "user@email.com");
        Member member = new Member("user@email.com", "123");
        given(noticeRepository.findByIdAndDeletedIsFalse(anyLong()))
                .willReturn(Optional.of(new Notice("제목", "내용", member)));
        given(this.memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member));

        // when
        NoticeResponse noticeResponse = this.noticeService.updateNotice(1L, noticeRequest, loginUser);

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
        Member member = new Member("user@email.com", "123");
        given(noticeRepository.findByIdAndDeletedIsFalse(anyLong()))
                .willReturn(Optional.of(new Notice("제목", "내용", member)));
        given(this.memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(new Member("user1@email.com", "123")));

        // when - then
        assertThatThrownBy(() ->
                this.noticeService.updateNotice(1L,
                        new NoticeRequest("제목1", "내용1"), new LoginUser(1L, "user@email.com")))
                .isInstanceOf(NoticePermissionDeniedException.class)
                .hasMessage("공지사항 수정 권한이 없습니다.");
    }

    @Test
    @DisplayName("공지사항 삭제 서비스")
    void notice_remove() {
        // given
        Member member = new Member("user@email.com", "123");
        given(this.noticeRepository.findByIdAndDeletedIsFalse(anyLong()))
                .willReturn(Optional.of(new Notice("제목", "내용", member)));
        given(this.memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(new Member("user@email.com", "123")));

        // when
        this.noticeService.deleteNotice(1L, new LoginUser(1L, "user@email.com"));

        // then
        verify(this.noticeRepository).delete(any(Notice.class));
    }

    @Test
    @DisplayName("페이징 처리된 목록 조회")
    void notice_findAll_with_paging() {
        // given
        Member member = new Member("user@email.com", "123");
        Pageable pageable = PageRequest.of(0, 3);
        Page<Notice> notices = new PageImpl<>(Arrays.asList(new Notice("제목", "내용", member)));
        given(this.noticeRepository.findAllByDeletedIsFalse(pageable)).willReturn(notices);

        // when
        Page<NoticeResponse> noticeResponsesWithPaging = this.noticeService.findAllNoticesWithPaging(pageable);

        // then
        assertThat(noticeResponsesWithPaging.getContent()).size().isOne();
    }
}

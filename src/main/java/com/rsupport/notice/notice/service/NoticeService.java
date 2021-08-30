package com.rsupport.notice.notice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rsupport.notice.auth.domain.LoginUser;
import com.rsupport.notice.auth.exception.AuthorizationException;
import com.rsupport.notice.member.domain.Member;
import com.rsupport.notice.member.domain.MemberRepository;
import com.rsupport.notice.notice.domain.Notice;
import com.rsupport.notice.notice.domain.NoticeRepository;
import com.rsupport.notice.notice.dto.NoticeRequest;
import com.rsupport.notice.notice.dto.NoticeResponse;
import com.rsupport.notice.notice.exception.NoticeNotFoundException;
import com.rsupport.notice.notice.exception.NoticePermissionDeniedException;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    public NoticeService(NoticeRepository noticeRepository, MemberRepository memberRepository) {
        this.noticeRepository = noticeRepository;
        this.memberRepository = memberRepository;
    }

    public NoticeResponse saveNotice(NoticeRequest noticeRequest, LoginUser loginUser) {
        Member member = findMemberByLoginUserOrExceptionWithMessage(loginUser, "등록 권한이 없습니다.");
        Notice savedNotice =
                this.noticeRepository.save(new Notice(noticeRequest.getTitle(), noticeRequest.getContent(), member));
        return NoticeResponse.of(savedNotice, savedNotice.hasSameOwner(member));
    }

    public NoticeResponse findNoticeBy(Long id, LoginUser loginUser) {
        Member findUser = findMemberByLoginUserOrExceptionWithMessage(loginUser, "사용자 조회에 실패하였습니다.");
        Notice findNotice = findNoticeByIdOrException(id);
        return NoticeResponse.of(findNotice, findNotice.hasSameOwner(findUser));
    }

    public NoticeResponse updateNotice(Long id, NoticeRequest updateNoticeRequest, LoginUser loginUser) {
        Notice findNotice = findNoticeByIdOrException(id);
        Member findUser = findMemberByLoginUserOrExceptionWithMessage(loginUser, "사용자 조회에 실패하였습니다.");
        validateNoticePermission(findNotice, findUser);
        findNotice.updateTo(updateNoticeRequest.toUpdateNotice());
        return NoticeResponse.of(findNotice, findNotice.hasSameOwner(findUser));
    }

    public void deleteNotice(long id, LoginUser loginUser) {
        Notice findNotice = findNoticeByIdOrException(id);
        Member findUser = findMemberByLoginUserOrExceptionWithMessage(loginUser, "사용자 조회에 실패하였습니다.");
        validateNoticePermission(findNotice, findUser);
        this.noticeRepository.delete(findNotice);
    }

    public Page<NoticeResponse> findAllNoticesWithPaging(Pageable pageable) {
        return this.noticeRepository.findAllByDeletedIsFalse(pageable)
                .map(NoticeResponse::of);
    }

    private Member findMemberByLoginUserOrExceptionWithMessage(LoginUser loginUser, String exceptionMessage) {
        return this.memberRepository.findByEmail(loginUser.getEmail())
                .orElseThrow(() -> new AuthorizationException(exceptionMessage));
    }

    private Notice findNoticeByIdOrException(Long id) {
        return this.noticeRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new NoticeNotFoundException("요청한 공지사항이 없습니다."));
    }

    private void validateNoticePermission(Notice findNotice, Member writer) {
        if (!findNotice.hasSameOwner(writer)) {
            throw new NoticePermissionDeniedException("공지사항 수정 권한이 없습니다.");
        }
    }
}

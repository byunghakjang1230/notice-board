package com.rsupport.notice.notice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DisplayName("NoticeRepository 단위 테스트")
@DataJpaTest
class NoticeRepositoryTest {
    @Autowired
    private NoticeRepository noticeRepository;

    @Test
    @DisplayName("JPA 기본 동작 확인")
    void default_save() {
        // when
        Notice saveNotice = noticeRepository.save(new Notice("hi", "register test", "user@email.com"));

        // then
        assertThat(saveNotice).isNotNull();
    }

    @TestFactory
    @DisplayName("공지사항 조회")
    List<DynamicTest> find_by_id_and_deleted_is_false() {
        // given
        Notice saveNotice = noticeRepository.save(new Notice("hi", "register test", "user@email.com"));

        // when
        Optional<Notice> findNotice = noticeRepository.findByIdAndDeletedIsFalse(saveNotice.getId());

        // then
        return Arrays.asList(
                dynamicTest("삭제되지 않은 공지사항 조회", () -> assertThat(findNotice).isNotEmpty()),
                dynamicTest("삭제 처리된 공지사항 조회되지 않음", () ->
                        findNotice.ifPresent(notice -> {
                            // when
                            notice.deleted();

                            // then
                            Optional<Notice> findDeletedNotice = noticeRepository.findByIdAndDeletedIsFalse(notice.getId());
                            assertThat(findDeletedNotice).isEmpty();
                        }))
        );
    }

    @Test
    @DisplayName("공지사항 목록 조회")
    void find_all_notices() {
        // given
        noticeRepository.saveAll(Arrays.asList(new Notice("hi1", "register test1", "user@email.com"),
                new Notice("hi2", "register test2", "user@email.com"),
                new Notice("hi3", "register test3", "user@email.com"),
                new Notice("hi4", "register test4", "user@email.com"),
                new Notice("hi5", "register test5", "user@email.com")));

        // when
        Page<Notice> noticePage = this.noticeRepository.findAllByDeletedIsFalse(PageRequest.of(1, 3));

        // then
        assertAll(
                () -> assertThat(noticePage.getContent().size()).isEqualTo(2),
                () -> assertThat(noticePage.getTotalElements()).isEqualTo(5),
                () -> assertThat(noticePage.getTotalPages()).isEqualTo(2),
                () -> assertThat(noticePage.getPageable().getPageNumber()).isEqualTo(1)
        );

    }
}

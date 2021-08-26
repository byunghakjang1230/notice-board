package com.rsupport.notice.domain;

import static org.assertj.core.api.Assertions.assertThat;
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

/**
 * NoticeRepository 테스트 코드 작성
 */
@DisplayName("NoticeRepository 테스트")
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
                            notice.deleted();
                            Optional<Notice> findDeletedNotice = noticeRepository.findByIdAndDeletedIsFalse(notice.getId());
                            assertThat(findDeletedNotice).isEmpty();
                        }))
        );
    }
}

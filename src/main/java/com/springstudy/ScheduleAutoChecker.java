package com.springstudy;

import com.springstudy.checker.SimilarityChecker;
import com.springstudy.domain.Schedule;
import com.springstudy.domain.UserCheck;
import com.springstudy.domain.UserLesson;
import com.springstudy.repository.ScheduleRepository;
import com.springstudy.repository.UserCheckRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleAutoChecker {

    private final SimilarityChecker similarityChecker;
    private final ScheduleRepository scheduleRepository;

    @Scheduled(fixedRate = 30_000) // 1분마다 실행
    @Transactional
    public void autoMarkDoneWork() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();

        List<Schedule> schedules = scheduleRepository.findByDate(today);

        if (schedules.isEmpty()) {
            return;
        }

        Schedule schedule = schedules.get(0);
        LocalTime progressCheckTime = schedule.getProgressCheckTime();
        // ✅ 현재 시간에서 시/분만 추출해서 비교
        LocalTime nowTime = now.toLocalTime().withSecond(0).withNano(0);

        Duration diff = Duration.between(progressCheckTime, nowTime);
        if (Math.abs(diff.toMinutes()) > 1) {
            return;
        }


        for (Schedule sch : schedules) {
            LocalDateTime schTime = LocalDateTime.of(sch.getDate(), sch.getProgressCheckTime());
            if (now.isBefore(schTime)) continue; // 아직 시간이 안 됐으면 스킵

            String targetLesson = sch.getLesson().getTitle();

            for (UserCheck uc : sch.getUserChecks()) {
                if (uc.isDoneWork()) continue;

                Set<String> validLessons = uc.getUser().getUserLessons().stream()
                        .filter(ul -> !ul.isUsedForCheck()) // 사용되지 않은 것만
                        .map(UserLesson::getLesson)
                        .collect(Collectors.toSet());

                boolean matched = validLessons.stream()
                        .anyMatch(title -> similarityChecker.isSimilar(title, targetLesson));

                if (matched) {
                    uc.doneWork();
                    // ✅ 매치된 UserLesson 플래그 변경
                    uc.getUser().getUserLessons().stream()
                            .filter(ul -> similarityChecker.isSimilar(ul.getLesson(), targetLesson))
                            .forEach(ul -> ul.setUsedForCheck(true));
                } else {

                }

            log.info("✅ 자동 체크 완료: User={}, Lesson={}, Time={}", uc.getUser().getUserName(), targetLesson, schTime);
            }
        }

        // 변경된 UserCheck들은 JPA dirty checking으로 자동 반영
    }
}
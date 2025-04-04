package com.springstudy.service;

import com.springstudy.domain.DayTime;
import com.springstudy.repository.DayTimeRepository;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DayTimeService {

    private final DayTimeRepository dayTimeRepository;

    public DayTimeService(DayTimeRepository dayTimeRepository) {
        this.dayTimeRepository = dayTimeRepository;
    }

    // 초기 데이터 설정 (DB에 없으면 삽입)
    @Transactional
    public void initializeDefaultTimes() {
        Map<DayOfWeek, LocalTime> defaults = new EnumMap<>(DayOfWeek.class);
        defaults.put(DayOfWeek.MONDAY, LocalTime.of(23, 59));
        defaults.put(DayOfWeek.TUESDAY, LocalTime.of(23, 59));
        defaults.put(DayOfWeek.WEDNESDAY, LocalTime.of(23, 59));
        defaults.put(DayOfWeek.THURSDAY, LocalTime.of(23, 59));
        defaults.put(DayOfWeek.FRIDAY, LocalTime.of(23, 59));
        defaults.put(DayOfWeek.SATURDAY, LocalTime.of(11, 0));
        defaults.put(DayOfWeek.SUNDAY, LocalTime.of(11, 0));

        defaults.forEach((day, time) -> {
            if (dayTimeRepository.findByDay(day).isEmpty()) {
                dayTimeRepository.save(new DayTime(day, time));
            }
        });
    }

    // 모든 요일별 시간 조회
    public List<DayTime> getAllDefaultTimes() {
        return dayTimeRepository.findAll();
    }

    // 특정 요일의 시간 업데이트
    @Transactional
    public DayTime updateDefaultTime(DayOfWeek day, LocalTime newTime) {
        DayTime dayTime = dayTimeRepository.findByDay(day)
                .orElseThrow(() -> new IllegalArgumentException("해당 요일이 존재하지 않습니다: " + day));
        dayTime.setDefaultTime(newTime);
        return dayTimeRepository.save(dayTime);
    }
}
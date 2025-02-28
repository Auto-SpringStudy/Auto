package com.springstudy.service;

import com.springstudy.domain.Lecture;
import com.springstudy.dto.LectureDTO;
import com.springstudy.dto.LessonDTO;
import com.springstudy.dto.SectionDTO;
import com.springstudy.repository.LectureRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;

    @Transactional(readOnly = true)
    public List<LectureDTO> getAllLectures() {
        List<Lecture> lectures = lectureRepository.findAll();
        return lectures.stream().map(lecture -> {
            List<SectionDTO> sectionDTOs = lecture.getSections().stream().map(section -> {
                List<LessonDTO> lessonDTOs = section.getLessons().stream()
                        .map(lesson -> new LessonDTO(lesson.getId(), lesson.getTitle(), lesson.getDuration()))
                        .collect(Collectors.toList());
                return new SectionDTO(section.getId(), section.getTitle(), lessonDTOs);
            }).collect(Collectors.toList());
            return new LectureDTO(lecture.getId(), lecture.getTitle(), sectionDTOs);
        }).collect(Collectors.toList());
    }

    public LectureDTO getLecture(Long lectureId) {
        // 1) Lecture 엔티티 조회
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("Lecture not found, id=" + lectureId));

        // 2) Lecture → LectureDTO 변환
        //    섹션, 레슨도 각각 SectionDTO, LessonDTO로 맵핑
        List<SectionDTO> sectionDTOs = lecture.getSections().stream()
                .map(section -> {
                    List<LessonDTO> lessonDTOs = section.getLessons().stream()
                            .map(lesson -> new LessonDTO(
                                    lesson.getId(),
                                    lesson.getTitle(),
                                    lesson.getDuration() // "HH:MM:SS" 형식이라면 그대로 String으로
                            ))
                            .collect(Collectors.toList());
                    return new SectionDTO(section.getId(), section.getTitle(), lessonDTOs);
                })
                .collect(Collectors.toList());

        // LectureDTO(lectureId, lectureTitle, List<SectionDTO>)
        return new LectureDTO(
                lecture.getId(),
                lecture.getTitle(),
                sectionDTOs
        );
    }
}

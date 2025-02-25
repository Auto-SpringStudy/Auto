package com.springstudy.controller;

import com.springstudy.domain.Lecture;
import com.springstudy.domain.Lesson;
import com.springstudy.domain.Section;
import com.springstudy.dto.LectureDTO;
import com.springstudy.dto.LectureRequest;
import com.springstudy.dto.LessonRequest;
import com.springstudy.dto.SectionRequest;
import com.springstudy.repository.LectureRepository;
import com.springstudy.service.LectureService;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lectures")
@RequiredArgsConstructor
public class LectureController {
    private final LectureRepository lectureRepository;
    private final LectureService lectureService;

    @PostMapping("/add")
    public ResponseEntity<String> createLecture(@RequestBody LectureRequest lectureRequest) {
        Lecture lecture = new Lecture();
        lecture.setTitle(lectureRequest.getTitle());

        for (SectionRequest sectionRequest : lectureRequest.getSections()) {
            Section section = new Section();
            section.setTitle(sectionRequest.getTitle());
            section.setLecture(lecture);

            for (LessonRequest lessonRequest : sectionRequest.getLessons()) {
                Lesson lesson = new Lesson();
                lesson.setTitle(lessonRequest.getTitle());
                lesson.setDuration(LocalTime.parse(lessonRequest.getDuration()));
                lesson.setSection(section);
                section.getLessons().add(lesson);
            }
            lecture.getSections().add(section);
        }

        lectureRepository.save(lecture);
        return ResponseEntity.ok("강의 저장 완료!");
    }

    @GetMapping
    public List<Lecture> getAllLectures() {
        return lectureRepository.findAll();
    }

    @GetMapping
    public String showLectureList(Model model) {
        List<LectureDTO> lectures = lectureService.getAllLectures();
        model.addAttribute("lectures", lectures);
        return "lecture-list";
    }
}

package com.springstudy.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureDTO {
    private Long lectureId;
    private String lectureTitle;
    private List<SectionDTO> sections;
}




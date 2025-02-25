package com.springstudy.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionDTO {
    private Long sectionId;
    private String sectionTitle;
    private List<LessonDTO> lessons;
}
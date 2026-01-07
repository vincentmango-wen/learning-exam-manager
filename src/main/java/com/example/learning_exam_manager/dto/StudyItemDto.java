package com.example.learning_exam_manager.dto;

import com.example.learning_exam_manager.entity.StudyItem;

public class StudyItemDto {
    private Long id;
    private Long subjectId;
    private String subjectName;
    private String title;
    private StudyItem.Status status;
    private Integer studyTime;
    
    public StudyItemDto() {}
    
    public StudyItemDto(Long id, Long subjectId, String subjectName, String title, StudyItem.Status status, Integer studyTime) {
        this.id = id;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.title = title;
        this.status = status;
        this.studyTime = studyTime;
    }
    
    // Getter/Setter
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getSubjectId() {
        return subjectId;
    }
    
    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }
    
    public String getSubjectName() {
        return subjectName;
    }
    
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public StudyItem.Status getStatus() {
        return status;
    }
    
    public void setStatus(StudyItem.Status status) {
        this.status = status;
    }
    
    public Integer getStudyTime() {
        return studyTime;
    }
    
    public void setStudyTime(Integer studyTime) {
        this.studyTime = studyTime;
    }
}


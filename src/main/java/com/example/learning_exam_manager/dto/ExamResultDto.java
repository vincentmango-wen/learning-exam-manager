package com.example.learning_exam_manager.dto;

import java.time.LocalDate;

public class ExamResultDto {
    private Long id;
    private Long examId;
    private String examName;
    private String subjectName;
    private Integer score;
    private Boolean passed;
    private LocalDate takenAt;
    
    public ExamResultDto() {}
    
    public ExamResultDto(Long id, Long examId, String examName, String subjectName, Integer score, Boolean passed, LocalDate takenAt) {
        this.id = id;
        this.examId = examId;
        this.examName = examName;
        this.subjectName = subjectName;
        this.score = score;
        this.passed = passed;
        this.takenAt = takenAt;
    }
    
    // Getter/Setter
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getExamId() {
        return examId;
    }
    
    public void setExamId(Long examId) {
        this.examId = examId;
    }
    
    public String getExamName() {
        return examName;
    }
    
    public void setExamName(String examName) {
        this.examName = examName;
    }
    
    public String getSubjectName() {
        return subjectName;
    }
    
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    
    public Integer getScore() {
        return score;
    }
    
    public void setScore(Integer score) {
        this.score = score;
    }
    
    public Boolean getPassed() {
        return passed;
    }
    
    public void setPassed(Boolean passed) {
        this.passed = passed;
    }
    
    public LocalDate getTakenAt() {
        return takenAt;
    }
    
    public void setTakenAt(LocalDate takenAt) {
        this.takenAt = takenAt;
    }
}


package com.example.learning_exam_manager.dto;

public class ExamDto {
    private Long id;
    private Long subjectId;
    private String subjectName;
    private String examName;
    private Integer maxScore;
    private Integer passingScore;
    
    public ExamDto() {}
    
    public ExamDto(Long id, Long subjectId, String subjectName, String examName, Integer maxScore, Integer passingScore) {
        this.id = id;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.examName = examName;
        this.maxScore = maxScore;
        this.passingScore = passingScore;
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
    
    public String getExamName() {
        return examName;
    }
    
    public void setExamName(String examName) {
        this.examName = examName;
    }
    
    public Integer getMaxScore() {
        return maxScore;
    }
    
    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }
    
    public Integer getPassingScore() {
        return passingScore;
    }
    
    public void setPassingScore(Integer passingScore) {
        this.passingScore = passingScore;
    }
}


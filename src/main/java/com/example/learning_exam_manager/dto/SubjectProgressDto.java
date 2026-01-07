package com.example.learning_exam_manager.dto;

public class SubjectProgressDto {
    private Long subjectId;
    private String subjectName;
    private double progressRate;
    
    public SubjectProgressDto() {}
    
    public SubjectProgressDto(Long subjectId, String subjectName, double progressRate) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.progressRate = progressRate;
    }
    
    // Getter/Setter
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
    
    public double getProgressRate() {
        return progressRate;
    }
    
    public void setProgressRate(double progressRate) {
        this.progressRate = progressRate;
    }
}
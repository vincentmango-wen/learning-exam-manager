package com.example.learning_exam_manager.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ExamForm {
    
    @NotNull(message = "科目は必須です")
    private Long subjectId;
    
    @NotBlank(message = "試験名は必須です")
    @Size(max = 200, message = "試験名は200文字以内で入力してください")
    private String examName;
    
    @NotNull(message = "満点は必須です")
    @Min(value = 1, message = "満点は1以上で入力してください")
    @Max(value = 1000, message = "満点は1000以下で入力してください")
    private Integer maxScore = 100;
    
    @NotNull(message = "合格点は必須です")
    @Min(value = 0, message = "合格点は0以上で入力してください")
    private Integer passingScore = 60;
    
    public ExamForm() {}
    
    public ExamForm(Long subjectId, String examName, Integer maxScore, Integer passingScore) {
        this.subjectId = subjectId;
        this.examName = examName;
        this.maxScore = maxScore;
        this.passingScore = passingScore;
    }
    
    // Getter/Setter
    public Long getSubjectId() {
        return subjectId;
    }
    
    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
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


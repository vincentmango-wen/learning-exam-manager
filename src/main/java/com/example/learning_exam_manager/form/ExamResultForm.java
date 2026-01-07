package com.example.learning_exam_manager.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class ExamResultForm {
    
    @NotNull(message = "試験は必須です")
    private Long examId;
    
    @NotNull(message = "点数は必須です")
    @Min(value = 0, message = "点数は0以上で入力してください")
    @Max(value = 1000, message = "点数は1000以下で入力してください")
    private Integer score;
    
    @NotNull(message = "受験日は必須です")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate takenAt;
    
    public ExamResultForm() {}
    
    public ExamResultForm(Long examId, Integer score, LocalDate takenAt) {
        this.examId = examId;
        this.score = score;
        this.takenAt = takenAt;
    }
    
    // Getter/Setter
    public Long getExamId() {
        return examId;
    }
    
    public void setExamId(Long examId) {
        this.examId = examId;
    }
    
    public Integer getScore() {
        return score;
    }
    
    public void setScore(Integer score) {
        this.score = score;
    }
    
    public LocalDate getTakenAt() {
        return takenAt;
    }
    
    public void setTakenAt(LocalDate takenAt) {
        this.takenAt = takenAt;
    }
}


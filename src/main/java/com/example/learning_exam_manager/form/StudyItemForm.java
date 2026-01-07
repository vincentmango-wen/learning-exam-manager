package com.example.learning_exam_manager.form;

import com.example.learning_exam_manager.entity.StudyItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class StudyItemForm {
    
    @NotNull(message = "科目は必須です")
    private Long subjectId;
    
    @NotBlank(message = "タイトルは必須です")
    @Size(max = 500, message = "タイトルは500文字以内で入力してください")
    private String title;
    
    private StudyItem.Status status = StudyItem.Status.NOT_STARTED;
    
    private Integer studyTime = 0;
    
    public StudyItemForm() {}
    
    public StudyItemForm(Long subjectId, String title, StudyItem.Status status, Integer studyTime) {
        this.subjectId = subjectId;
        this.title = title;
        this.status = status;
        this.studyTime = studyTime;
    }
    
    // Getter/Setter
    public Long getSubjectId() {
        return subjectId;
    }
    
    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
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


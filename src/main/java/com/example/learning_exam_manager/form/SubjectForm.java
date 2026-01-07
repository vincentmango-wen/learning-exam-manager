package com.example.learning_exam_manager.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SubjectForm {
    
    @NotBlank(message = "科目名は必須です")
    @Size(max = 200, message = "科目名は200文字以内で入力してください")
    private String name;
    
    private String description;
    
    public SubjectForm() {}
    
    public SubjectForm(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    // Getter/Setter
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}
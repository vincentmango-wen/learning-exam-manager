package com.example.learning_exam_manager.dto;

public class DashboardSummaryDto {
    private Long totalSubjects;
    private Long completedSubjects;
    private Long pendingSubjects;
    private Double overallProgressRate;
    private Long retestExamCount;
    
    public DashboardSummaryDto() {}
    
    public DashboardSummaryDto(Long totalSubjects, Long completedSubjects, 
                              Long pendingSubjects, Double overallProgressRate, 
                              Long retestExamCount) {
        this.totalSubjects = totalSubjects;
        this.completedSubjects = completedSubjects;
        this.pendingSubjects = pendingSubjects;
        this.overallProgressRate = overallProgressRate;
        this.retestExamCount = retestExamCount;
    }
    
    // Getters and Setters
    public Long getTotalSubjects() {
        return totalSubjects;
    }
    
    public void setTotalSubjects(Long totalSubjects) {
        this.totalSubjects = totalSubjects;
    }
    
    public Long getCompletedSubjects() {
        return completedSubjects;
    }
    
    public void setCompletedSubjects(Long completedSubjects) {
        this.completedSubjects = completedSubjects;
    }
    
    public Long getPendingSubjects() {
        return pendingSubjects;
    }
    
    public void setPendingSubjects(Long pendingSubjects) {
        this.pendingSubjects = pendingSubjects;
    }
    
    public Double getOverallProgressRate() {
        return overallProgressRate;
    }
    
    public void setOverallProgressRate(Double overallProgressRate) {
        this.overallProgressRate = overallProgressRate;
    }
    
    public Long getRetestExamCount() {
        return retestExamCount;
    }
    
    public void setRetestExamCount(Long retestExamCount) {
        this.retestExamCount = retestExamCount;
    }
}
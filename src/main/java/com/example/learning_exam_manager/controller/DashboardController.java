package com.example.learning_exam_manager.controller;

import com.example.learning_exam_manager.dto.ExamResultDto;
import com.example.learning_exam_manager.dto.SubjectProgressDto;
import com.example.learning_exam_manager.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {
    
    private final DashboardService dashboardService;
    
    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }
    
    @GetMapping("/")
    public String index(Model model) {
        List<SubjectProgressDto> subjectProgress = dashboardService.getAllSubjectProgress();
        List<ExamResultDto> recentExamResults = dashboardService.getRecentExamResults();
        
        model.addAttribute("subjectProgress", subjectProgress);
        model.addAttribute("recentExamResults", recentExamResults);
        return "dashboard/index";
    }
}


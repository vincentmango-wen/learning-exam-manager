package com.example.learning_exam_manager.controller;

import com.example.learning_exam_manager.dto.DashboardSummaryDto;
import com.example.learning_exam_manager.dto.ExamResultDto;
import com.example.learning_exam_manager.dto.SubjectProgressDto;
import com.example.learning_exam_manager.service.DashboardService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {
    
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    
    private final DashboardService dashboardService;
    private final ObjectMapper objectMapper;
    
    @Autowired
    public DashboardController(DashboardService dashboardService, ObjectMapper objectMapper) {
        this.dashboardService = dashboardService;
        this.objectMapper = objectMapper;
    }
    
    @GetMapping("/")
    public String index(Model model) throws JsonProcessingException {
        logger.debug("ダッシュボードを表示します");
        List<SubjectProgressDto> subjectProgress = dashboardService.getAllSubjectProgress();
        List<ExamResultDto> recentExamResults = dashboardService.getRecentExamResults();
        
        DashboardSummaryDto summary = dashboardService.getDashboardSummary();
        model.addAttribute("dashboardSummary", summary);
        
        model.addAttribute("subjectProgress", subjectProgress);
        model.addAttribute("recentExamResults", recentExamResults);
        
        // グラフ用のJSON文字列を生成
        List<String> subjectNames = subjectProgress.stream()
                .map(SubjectProgressDto::getSubjectName)
                .collect(Collectors.toList());
        List<Double> progressRates = subjectProgress.stream()
                .map(SubjectProgressDto::getProgressRate)
                .collect(Collectors.toList());
        
        model.addAttribute("subjectNamesJson", objectMapper.writeValueAsString(subjectNames));
        model.addAttribute("progressRatesJson", objectMapper.writeValueAsString(progressRates));
        
        List<String> takenDates = recentExamResults.stream()
                .map(result -> result.getTakenAt().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .collect(Collectors.toList());
        List<Integer> scores = recentExamResults.stream()
                .map(ExamResultDto::getScore)
                .collect(Collectors.toList());
        
        model.addAttribute("takenDatesJson", objectMapper.writeValueAsString(takenDates));
        model.addAttribute("scoresJson", objectMapper.writeValueAsString(scores));
        
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("title", "ダッシュボード");
        logger.info("ダッシュボードを表示しました: 科目数={}, 試験結果数={}", 
                subjectProgress.size(), recentExamResults.size());
        return "dashboard/index";
    }
}


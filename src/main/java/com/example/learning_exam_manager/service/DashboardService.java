package com.example.learning_exam_manager.service;

import com.example.learning_exam_manager.dto.ExamResultDto;
import com.example.learning_exam_manager.dto.SubjectProgressDto;
import com.example.learning_exam_manager.entity.StudyItem;
import com.example.learning_exam_manager.entity.Subject;
import com.example.learning_exam_manager.repository.StudyItemRepository;
import com.example.learning_exam_manager.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DashboardService {
    
    private final SubjectRepository subjectRepository;
    private final StudyItemRepository studyItemRepository;
    private final ExamResultService examResultService;
    
    @Autowired
    public DashboardService(SubjectRepository subjectRepository, 
                           StudyItemRepository studyItemRepository,
                           ExamResultService examResultService) {
        this.subjectRepository = subjectRepository;
        this.studyItemRepository = studyItemRepository;
        this.examResultService = examResultService;
    }
    
    @Transactional(readOnly = true)
    public List<SubjectProgressDto> getAllSubjectProgress() {
        List<Subject> subjects = subjectRepository.findAll();
        return subjects.stream()
                .map(this::calculateProgress)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public SubjectProgressDto getSubjectProgress(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("科目が見つかりません: " + subjectId));
        return calculateProgress(subject);
    }
    
    @Transactional(readOnly = true)
    public List<ExamResultDto> getRecentExamResults() {
        return examResultService.findRecentResults();
    }
    
    private SubjectProgressDto calculateProgress(Subject subject) {
        List<StudyItem> studyItems = studyItemRepository.findBySubjectId(subject.getId());
        
        if (studyItems.isEmpty()) {
            return new SubjectProgressDto(subject.getId(), subject.getName(), 0.0);
        }
        
        long doneCount = studyItems.stream()
                .filter(item -> item.getStatus() == StudyItem.Status.DONE)
                .count();
        
        double progressRate = (double) doneCount / studyItems.size() * 100.0;
        
        return new SubjectProgressDto(subject.getId(), subject.getName(), progressRate);
    }
}


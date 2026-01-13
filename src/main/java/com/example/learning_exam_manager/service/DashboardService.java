package com.example.learning_exam_manager.service;

import com.example.learning_exam_manager.dto.ExamResultDto;
import com.example.learning_exam_manager.dto.SubjectProgressDto;
import com.example.learning_exam_manager.entity.StudyItem;
import com.example.learning_exam_manager.entity.Subject;
import com.example.learning_exam_manager.exception.ResourceNotFoundException;
import com.example.learning_exam_manager.repository.StudyItemRepository;
import com.example.learning_exam_manager.repository.SubjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DashboardService {
    
    private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);
    
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
        logger.debug("すべての科目の進捗率を取得します");
        List<Subject> subjects = subjectRepository.findAll();
        List<SubjectProgressDto> result = subjects.stream()
                .map(this::calculateProgress)
                .collect(Collectors.toList());
        logger.info("科目の進捗率を{}件取得しました", result.size());
        return result;
    }
    
    @Transactional(readOnly = true)
    public SubjectProgressDto getSubjectProgress(Long subjectId) {
        logger.debug("科目の進捗率を取得します: subjectId={}", subjectId);
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("科目が見つかりません: " + subjectId));
        SubjectProgressDto result = calculateProgress(subject);
        logger.info("科目の進捗率を取得しました: subjectId={}, 進捗率={}%", subjectId, result.getProgressRate());
        return result;
    }
    
    @Transactional(readOnly = true)
    public List<ExamResultDto> getRecentExamResults() {
        logger.debug("直近の試験結果を取得します");
        List<ExamResultDto> result = examResultService.findRecentResults();
        logger.info("直近の試験結果を{}件取得しました", result.size());
        return result;
    }
    
    private SubjectProgressDto calculateProgress(Subject subject) {
        logger.debug("進捗率を計算します: subjectId={}, subjectName={}", subject.getId(), subject.getName());
        List<StudyItem> studyItems = studyItemRepository.findBySubjectId(subject.getId());
        
        if (studyItems.isEmpty()) {
            logger.debug("学習項目が存在しないため、進捗率は0%です: subjectId={}", subject.getId());
            return new SubjectProgressDto(subject.getId(), subject.getName(), 0.0);
        }
        
        long doneCount = studyItems.stream()
                .filter(item -> item.getStatus() == StudyItem.Status.DONE)
                .count();
        
        double progressRate = (double) doneCount / studyItems.size() * 100.0;
        logger.debug("進捗率を計算しました: subjectId={}, 完了数={}, 総数={}, 進捗率={}%", 
                subject.getId(), doneCount, studyItems.size(), progressRate);
        
        return new SubjectProgressDto(subject.getId(), subject.getName(), progressRate);
    }
}


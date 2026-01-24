package com.example.learning_exam_manager.service;

import com.example.learning_exam_manager.dto.DashboardSummaryDto;
import com.example.learning_exam_manager.dto.ExamResultDto;
import com.example.learning_exam_manager.dto.SubjectProgressDto;
import com.example.learning_exam_manager.entity.StudyItem;
import com.example.learning_exam_manager.entity.Subject;
import com.example.learning_exam_manager.exception.ResourceNotFoundException;
import com.example.learning_exam_manager.repository.StudyItemRepository;
import com.example.learning_exam_manager.repository.SubjectRepository;
import com.example.learning_exam_manager.repository.ExamResultRepository;
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
    private final ExamResultRepository examResultRepository;
    
    @Autowired
    public DashboardService(SubjectRepository subjectRepository, 
                           StudyItemRepository studyItemRepository,
                           ExamResultService examResultService,
                           ExamResultRepository examResultRepository) {
        this.subjectRepository = subjectRepository;
        this.studyItemRepository = studyItemRepository;
        this.examResultService = examResultService;
        this.examResultRepository = examResultRepository;
    }
    

    // サマリー情報を取得するメソッド
    @Transactional(readOnly = true)
    public DashboardSummaryDto getDashboardSummary() {
        logger.debug("ダッシュボードサマリー情報を取得します");
        
        List<Subject> allSubjects = subjectRepository.findAll();
        Long totalSubjects = (long) allSubjects.size();
        
        Long completedSubjects = allSubjects.stream()
                .filter(subject -> {
                    List<StudyItem> studyItems = studyItemRepository.findBySubjectId(subject.getId());
                    if (studyItems.isEmpty()) {
                        return false;
                    }
                    return studyItems.stream()
                            .allMatch(item -> item.getStatus() == StudyItem.Status.DONE);
                })
                .count();
        
        Long pendingSubjects = totalSubjects - completedSubjects;
        
        Double overallProgressRate = 0.0;
        if (totalSubjects > 0) {
            double rawRate = (double) completedSubjects / totalSubjects * 100.0;
            overallProgressRate = Math.round(rawRate * 10.0) / 10.0;
        }
        
        Long retestExamCount = (long) examResultRepository.findByPassed(false).size();
        
        DashboardSummaryDto summary = new DashboardSummaryDto(
                totalSubjects,
                completedSubjects,
                pendingSubjects,
                overallProgressRate,
                retestExamCount
        );
        
        logger.info("ダッシュボードサマリー情報を取得しました: 全科目数={}, 完了科目数={}, 未達科目数={}, 進捗率={}%, 再受験必要試験数={}", 
                totalSubjects, completedSubjects, pendingSubjects, overallProgressRate, retestExamCount);
        
        return summary;
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


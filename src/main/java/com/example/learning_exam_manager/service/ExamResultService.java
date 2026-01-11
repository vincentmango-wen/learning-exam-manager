package com.example.learning_exam_manager.service;

import com.example.learning_exam_manager.dto.ExamResultDto;
import com.example.learning_exam_manager.entity.Exam;
import com.example.learning_exam_manager.entity.ExamResult;
import com.example.learning_exam_manager.form.ExamResultForm;
import com.example.learning_exam_manager.repository.ExamRepository;
import com.example.learning_exam_manager.repository.ExamResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExamResultService {
    
    private final ExamResultRepository examResultRepository;
    private final ExamRepository examRepository;
    
    @Autowired
    public ExamResultService(ExamResultRepository examResultRepository, ExamRepository examRepository) {
        this.examResultRepository = examResultRepository;
        this.examRepository = examRepository;
    }
    
    @Transactional(readOnly = true)
    public List<ExamResultDto> findAll() {
        return examResultRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ExamResultDto> findAll(Pageable pageable) {
        return examResultRepository.findAll(pageable).map(this::toDto);
    }
    
    @Transactional(readOnly = true)
    public List<ExamResultDto> findByExamId(Long examId) {
        return examResultRepository.findByExamId(examId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ExamResultDto> findRecentResults() {
        return examResultRepository.findRecentResults().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ExamResultDto> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            // キーワードが空の場合は全件取得
            return findAll();
        }
        return examResultRepository.findByExamExamNameContaining(keyword.trim()).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ExamResultDto> search(String keyword, Pageable pageable) {
        return examResultRepository.findByExamExamNameContaining(keyword.trim(), pageable).map(this::toDto);
    }
    
    @Transactional(readOnly = true)
    public ExamResultDto findById(Long id) {
        ExamResult examResult = examResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("試験結果が見つかりません: " + id));
        return toDto(examResult);
    }
    
    public ExamResultDto create(ExamResultForm form) {
        Exam exam = examRepository.findById(form.getExamId())
                .orElseThrow(() -> new RuntimeException("試験が見つかりません: " + form.getExamId()));
        
        // 合否を自動判定
        Boolean passed = form.getScore() >= exam.getPassingScore();
        
        ExamResult examResult = new ExamResult(
                exam,
                form.getScore(),
                passed,
                form.getTakenAt()
        );
        
        ExamResult saved = examResultRepository.save(examResult);
        return toDto(saved);
    }
    
    public ExamResultDto update(Long id, ExamResultForm form) {
        ExamResult examResult = examResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("試験結果が見つかりません: " + id));
        
        if (!examResult.getExam().getId().equals(form.getExamId())) {
            Exam exam = examRepository.findById(form.getExamId())
                    .orElseThrow(() -> new RuntimeException("試験が見つかりません: " + form.getExamId()));
            examResult.setExam(exam);
        }
        
        examResult.setScore(form.getScore());
        examResult.setTakenAt(form.getTakenAt());
        
        // 合否を自動判定
        Boolean passed = form.getScore() >= examResult.getExam().getPassingScore();
        examResult.setPassed(passed);
        
        ExamResult updated = examResultRepository.save(examResult);
        return toDto(updated);
    }
    
    public void delete(Long id) {
        examResultRepository.deleteById(id);
    }
    
    private ExamResultDto toDto(ExamResult entity) {
        return new ExamResultDto(
                entity.getId(),
                entity.getExam().getId(),
                entity.getExam().getExamName(),
                entity.getExam().getSubject().getName(),
                entity.getScore(),
                entity.getPassed(),
                entity.getTakenAt()
        );
    }
}


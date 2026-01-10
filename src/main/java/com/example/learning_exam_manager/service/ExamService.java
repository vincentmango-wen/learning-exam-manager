package com.example.learning_exam_manager.service;

import com.example.learning_exam_manager.dto.ExamDto;
import com.example.learning_exam_manager.entity.Exam;
import com.example.learning_exam_manager.entity.Subject;
import com.example.learning_exam_manager.form.ExamForm;
import com.example.learning_exam_manager.repository.ExamRepository;
import com.example.learning_exam_manager.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExamService {
    
    private final ExamRepository examRepository;
    private final SubjectRepository subjectRepository;
    
    @Autowired
    public ExamService(ExamRepository examRepository, SubjectRepository subjectRepository) {
        this.examRepository = examRepository;
        this.subjectRepository = subjectRepository;
    }
    
    @Transactional(readOnly = true)
    public List<ExamDto> findAll() {
        return examRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ExamDto> findBySubjectId(Long subjectId) {
        return examRepository.findBySubjectId(subjectId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ExamDto> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            // キーワードが空の場合は全件取得
            return findAll();
        }
        return examRepository.findByExamNameContaining(keyword.trim()).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ExamDto findById(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("試験が見つかりません: " + id));
        return toDto(exam);
    }
    
    public ExamDto create(ExamForm form) {
        Subject subject = subjectRepository.findById(form.getSubjectId())
                .orElseThrow(() -> new RuntimeException("科目が見つかりません: " + form.getSubjectId()));
        
        Exam exam = new Exam(
                subject,
                form.getExamName(),
                form.getMaxScore() != null ? form.getMaxScore() : 100,
                form.getPassingScore() != null ? form.getPassingScore() : 60
        );
        
        Exam saved = examRepository.save(exam);
        return toDto(saved);
    }
    
    public ExamDto update(Long id, ExamForm form) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("試験が見つかりません: " + id));
        
        if (!exam.getSubject().getId().equals(form.getSubjectId())) {
            Subject subject = subjectRepository.findById(form.getSubjectId())
                    .orElseThrow(() -> new RuntimeException("科目が見つかりません: " + form.getSubjectId()));
            exam.setSubject(subject);
        }
        
        exam.setExamName(form.getExamName());
        exam.setMaxScore(form.getMaxScore() != null ? form.getMaxScore() : 100);
        exam.setPassingScore(form.getPassingScore() != null ? form.getPassingScore() : 60);
        
        Exam updated = examRepository.save(exam);
        return toDto(updated);
    }
    
    public void delete(Long id) {
        examRepository.deleteById(id);
    }
    
    private ExamDto toDto(Exam entity) {
        return new ExamDto(
                entity.getId(),
                entity.getSubject().getId(),
                entity.getSubject().getName(),
                entity.getExamName(),
                entity.getMaxScore(),
                entity.getPassingScore()
        );
    }
}


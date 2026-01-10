package com.example.learning_exam_manager.service;

import com.example.learning_exam_manager.dto.SubjectDto;
import com.example.learning_exam_manager.entity.Subject;
import com.example.learning_exam_manager.form.SubjectForm;
import com.example.learning_exam_manager.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubjectService {
    
    private final SubjectRepository subjectRepository;
    
    @Autowired
    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }
    
    @Transactional(readOnly = true)
    public List<SubjectDto> findAll() {
        return subjectRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<SubjectDto> findAll(Pageable pageable) {
        return subjectRepository.findAll(pageable)
                .map(this::toDto);
    }
    
    @Transactional(readOnly = true)
    public Page<SubjectDto> searchByName(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll(pageable);
        }
        return subjectRepository.findByNameContaining(keyword.trim(), pageable)
                .map(this::toDto);
    }

    @Transactional(readOnly = true)
    public List<SubjectDto> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            // キーワードが空の場合は全件取得
            return findAll();
        }
        return subjectRepository.findByNameContaining(keyword.trim()).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public SubjectDto findById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("科目が見つかりません: " + id));
        return toDto(subject);
    }
    
    public SubjectDto create(SubjectForm form) {
        Subject subject = new Subject(form.getName(), form.getDescription());
        Subject saved = subjectRepository.save(subject);
        return toDto(saved);
    }
    
    public SubjectDto update(Long id, SubjectForm form) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("科目が見つかりません: " + id));
        subject.setName(form.getName());
        subject.setDescription(form.getDescription());
        Subject updated = subjectRepository.save(subject);
        return toDto(updated);
    }
    
    public void delete(Long id) {
        subjectRepository.deleteById(id);
    }
    
    private SubjectDto toDto(Subject entity) {
        return new SubjectDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription()
        );
    }
}
package com.example.learning_exam_manager.service;

import com.example.learning_exam_manager.dto.StudyItemDto;
import com.example.learning_exam_manager.entity.StudyItem;
import com.example.learning_exam_manager.entity.Subject;
import com.example.learning_exam_manager.form.StudyItemForm;
import com.example.learning_exam_manager.repository.StudyItemRepository;
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
public class StudyItemService {
    
    private final StudyItemRepository studyItemRepository;
    private final SubjectRepository subjectRepository;
    
    @Autowired
    public StudyItemService(StudyItemRepository studyItemRepository, SubjectRepository subjectRepository) {
        this.studyItemRepository = studyItemRepository;
        this.subjectRepository = subjectRepository;
    }
    
    @Transactional(readOnly = true)
    public List<StudyItemDto> findAll() {
        return studyItemRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<StudyItemDto> findAll(Pageable pageable) {
        return studyItemRepository.findAll(pageable).map(this::toDto);
    }

    @Transactional(readOnly = true)
    public List<StudyItemDto> findBySubjectId(Long subjectId) {
        return studyItemRepository.findBySubjectId(subjectId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudyItemDto> search(String keyword, Long subjectId) {
        if ((keyword == null || keyword.trim().isEmpty()) && subjectId == null) {
            // 検索条件がない場合は全件取得
            return findAll();
        }
        
        List<StudyItem> items;
        if (subjectId != null && keyword != null && !keyword.trim().isEmpty()) {
            // 科目IDとキーワードの両方で検索
            items = studyItemRepository.findBySubjectIdAndTitleContaining(subjectId, keyword.trim());
        } else if (subjectId != null) {
            // 科目IDのみで検索
            items = studyItemRepository.findBySubjectId(subjectId);
        } else {
            // キーワードのみで検索
            items = studyItemRepository.findByTitleContaining(keyword != null ? keyword.trim() : "");
        }
        
        return items.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<StudyItemDto> search(String keyword, Long subjectId, Pageable pageable) {
        Page<StudyItem> itemsPage;
        
        if (subjectId != null && keyword != null && !keyword.trim().isEmpty()) {
            // 科目IDとキーワードの両方で検索
            itemsPage = studyItemRepository.findBySubjectIdAndTitleContaining(
                subjectId, keyword.trim(), pageable);
        } else if (subjectId != null) {
            // 科目IDのみで検索
            itemsPage = studyItemRepository.findBySubjectId(subjectId, pageable);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            // キーワードのみで検索
            itemsPage = studyItemRepository.findByTitleContaining(keyword.trim(), pageable);
        } else {
            // 検索条件がない場合は全件取得（ページネーション付き）
            itemsPage = studyItemRepository.findAll(pageable);
        }
        
        return itemsPage.map(this::toDto);
    }

    @Transactional(readOnly = true)
    public StudyItemDto findById(Long id) {
        StudyItem studyItem = studyItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("学習項目が見つかりません: " + id));
        return toDto(studyItem);
    }
    
    public StudyItemDto create(StudyItemForm form) {
        Subject subject = subjectRepository.findById(form.getSubjectId())
                .orElseThrow(() -> new RuntimeException("科目が見つかりません: " + form.getSubjectId()));
        
        StudyItem studyItem = new StudyItem();
        studyItem.setSubject(subject);
        studyItem.setTitle(form.getTitle());
        studyItem.setStatus(form.getStatus());
        studyItem.setStudyTime(form.getStudyTime() != null ? form.getStudyTime() : 0);
        
        StudyItem saved = studyItemRepository.save(studyItem);
        return toDto(saved);
    }
    
    public StudyItemDto update(Long id, StudyItemForm form) {
        StudyItem studyItem = studyItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("学習項目が見つかりません: " + id));
        
        if (!studyItem.getSubject().getId().equals(form.getSubjectId())) {
            Subject subject = subjectRepository.findById(form.getSubjectId())
                    .orElseThrow(() -> new RuntimeException("科目が見つかりません: " + form.getSubjectId()));
            studyItem.setSubject(subject);
        }
        
        studyItem.setTitle(form.getTitle());
        studyItem.setStatus(form.getStatus());
        studyItem.setStudyTime(form.getStudyTime() != null ? form.getStudyTime() : 0);
        
        StudyItem updated = studyItemRepository.save(studyItem);
        return toDto(updated);
    }
    
    public void delete(Long id) {
        studyItemRepository.deleteById(id);
    }
    
    private StudyItemDto toDto(StudyItem entity) {
        return new StudyItemDto(
                entity.getId(),
                entity.getSubject().getId(),
                entity.getSubject().getName(),
                entity.getTitle(),
                entity.getStatus(),
                entity.getStudyTime()
        );
    }
}


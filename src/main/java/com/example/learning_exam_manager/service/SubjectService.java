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
import com.example.learning_exam_manager.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageImpl;
import java.util.Collections;
import com.example.learning_exam_manager.entity.StudyItem;
import com.example.learning_exam_manager.repository.StudyItemRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubjectService {

    private static final Logger logger = LoggerFactory.getLogger(SubjectService.class);
    
    private final SubjectRepository subjectRepository;
    private final StudyItemRepository studyItemRepository;
    
    @Autowired
    public SubjectService(SubjectRepository subjectRepository, StudyItemRepository studyItemRepository) {
        this.subjectRepository = subjectRepository;
        this.studyItemRepository = studyItemRepository;
    }

    @Transactional(readOnly = true)
    public Page<SubjectDto> findPendingSubjects(Pageable pageable) {
        logger.debug("未達科目を取得します");
        
        // 全科目を取得
        List<Subject> allSubjects = subjectRepository.findAll();
        
        // 未達科目をフィルタリング（全学習項目がDONEでない科目）
        List<Subject> pendingSubjects = allSubjects.stream()
                .filter(subject -> {
                    List<StudyItem> studyItems = studyItemRepository.findBySubjectId(subject.getId());
                    if (studyItems.isEmpty()) {
                        return true; // 学習項目が存在しない場合は未達
                    }
                    return !studyItems.stream()
                            .allMatch(item -> item.getStatus() == StudyItem.Status.DONE);
                })
                .collect(Collectors.toList());
        
        // Pageableに合わせてページング処理
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), pendingSubjects.size());
        List<Subject> pagedSubjects = start < pendingSubjects.size() 
                ? pendingSubjects.subList(start, end) 
                : Collections.emptyList();
        
        // DTOに変換
        List<SubjectDto> subjectDtos = pagedSubjects.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        
        return new PageImpl<>(subjectDtos, pageable, pendingSubjects.size());
    }
    
    @Transactional(readOnly = true)
    public List<SubjectDto> findAll() {
        logger.debug("すべての科目を取得します");
        List<SubjectDto> result = subjectRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        logger.info("科目を{}件取得しました", result.size());
        return result;
    }

    @Transactional(readOnly = true)
    public Page<SubjectDto> findAll(Pageable pageable) {
        logger.debug("科目一覧をページングして取得します");
        Page<Subject> result = subjectRepository.findAll(pageable);
        logger.info("科目を{}件取得しました", result.getTotalElements());
        return result.map(this::toDto);
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
                .orElseThrow(() -> new ResourceNotFoundException("科目が見つかりません: " + id));
        return toDto(subject);
    }
    
    public SubjectDto create(SubjectForm form) {
        logger.info("新しい科目を作成します: {}", form.getName());
        Subject subject = new Subject(form.getName(), form.getDescription());
        Subject saved = subjectRepository.save(subject);
        logger.info("科目を作成しました: ID={}, 名前={}", saved.getId(), saved.getName());
        return toDto(saved);
    }
    
    public SubjectDto update(Long id, SubjectForm form) {
        logger.info("科目を更新します: ID={}", id);
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("科目が見つかりません: " + id));
        subject.setName(form.getName());
        subject.setDescription(form.getDescription());
        Subject updated = subjectRepository.save(subject);
        logger.info("科目を更新しました: ID={}, 名前={}", updated.getId(), updated.getName());
        return toDto(updated);
    }
    
    public void delete(Long id) {
        logger.warn("科目を削除します: ID={}", id);
        subjectRepository.deleteById(id);
        logger.info("科目を削除しました: ID={}", id);
    }
    
    private SubjectDto toDto(Subject entity) {
        return new SubjectDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription()
        );
    }
}
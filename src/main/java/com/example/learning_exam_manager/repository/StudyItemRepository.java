package com.example.learning_exam_manager.repository;

import com.example.learning_exam_manager.entity.StudyItem;
import com.example.learning_exam_manager.entity.StudyItem.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface StudyItemRepository extends JpaRepository<StudyItem, Long> {
    
    List<StudyItem> findBySubjectId(Long subjectId);
    
    List<StudyItem> findBySubjectIdAndStatus(Long subjectId, Status status);

    List<StudyItem> findByTitleContaining(String title);
    
    List<StudyItem> findBySubjectIdAndTitleContaining(Long subjectId, String title);

    Page<StudyItem> findBySubjectId(Long subjectId, Pageable pageable);

    Page<StudyItem> findBySubjectIdAndStatus(Long subjectId, Status status, Pageable pageable);

    Page<StudyItem> findByTitleContaining(String title, Pageable pageable);

    Page<StudyItem> findBySubjectIdAndTitleContaining(Long subjectId, String title, Pageable pageable);
}
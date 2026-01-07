package com.example.learning_exam_manager.repository;

import com.example.learning_exam_manager.entity.StudyItem;
import com.example.learning_exam_manager.entity.StudyItem.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyItemRepository extends JpaRepository<StudyItem, Long> {
    
    List<StudyItem> findBySubjectId(Long subjectId);
    
    List<StudyItem> findBySubjectIdAndStatus(Long subjectId, Status status);
}
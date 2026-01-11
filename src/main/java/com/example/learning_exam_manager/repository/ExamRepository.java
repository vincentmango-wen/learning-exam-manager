package com.example.learning_exam_manager.repository;

import com.example.learning_exam_manager.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    
    List<Exam> findBySubjectId(Long subjectId);
    
    List<Exam> findByExamNameContaining(String examName);

    Page<Exam> findBySubjectId(Long subjectId, Pageable pageable);

    Page<Exam> findByExamNameContaining(String examName, Pageable pageable);
}
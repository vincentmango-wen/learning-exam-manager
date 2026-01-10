package com.example.learning_exam_manager.repository;

import com.example.learning_exam_manager.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    
    List<Exam> findBySubjectId(Long subjectId);
    
    // 試験名で部分一致検索
    List<Exam> findByExamNameContaining(String examName);
}
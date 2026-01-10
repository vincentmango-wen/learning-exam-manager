package com.example.learning_exam_manager.repository;

import com.example.learning_exam_manager.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    
    // 科目名で部分一致検索（Pageable対応）
    Page<Subject> findByNameContaining(String name, Pageable pageable);
    
    // List版も残す（必要に応じて）
    List<Subject> findByNameContaining(String name);
}
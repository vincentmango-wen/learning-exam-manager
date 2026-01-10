package com.example.learning_exam_manager.repository;

import com.example.learning_exam_manager.entity.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    
    List<ExamResult> findByExamId(Long examId);
    
    List<ExamResult> findByPassed(Boolean passed);
    
    @Query("SELECT er FROM ExamResult er ORDER BY er.takenAt DESC")
    List<ExamResult> findRecentResults();
    
    @Query("SELECT er FROM ExamResult er WHERE er.takenAt BETWEEN :startDate AND :endDate")
    List<ExamResult> findByTakenAtBetween(@Param("startDate") LocalDate startDate, 
                                          @Param("endDate") LocalDate endDate);
    
    // 試験名で部分一致検索（Examとの関連を通じて検索）
    @Query("SELECT er FROM ExamResult er WHERE er.exam.examName LIKE CONCAT('%', :examName, '%')")
    List<ExamResult> findByExamExamNameContaining(@Param("examName") String examName);
}
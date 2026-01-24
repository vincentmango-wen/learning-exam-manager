package com.example.learning_exam_manager.repository;

import com.example.learning_exam_manager.entity.StudyItem;
import com.example.learning_exam_manager.entity.StudyItem.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    
    Page<StudyItem> findByStatusNot(Status status, Pageable pageable);

    @Query("SELECT si FROM StudyItem si WHERE si.subject.id IN " +
           "(SELECT s.id FROM Subject s WHERE s.id NOT IN " +
           "(SELECT DISTINCT si2.subject.id FROM StudyItem si2 " +
           "WHERE si2.subject.id = s.id AND si2.status = :doneStatus " +
           "AND NOT EXISTS (SELECT 1 FROM StudyItem si3 WHERE si3.subject.id = s.id AND si3.status != :doneStatus)))")
    Page<StudyItem> findPendingStudyItems(@Param("doneStatus") Status doneStatus, Pageable pageable);
}
package edu.nju.onlineexam.dao;

import edu.nju.onlineexam.entity.ExamScoreEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamScoreRepository extends JpaRepository<ExamScoreEntity, Long> {
  ExamScoreEntity findByExamIdAndStudentId(long examId, long studentId);

  List<ExamScoreEntity> findByExamId(long examId);
}

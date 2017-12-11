package edu.nju.onlineexam.dao;

import edu.nju.onlineexam.entity.ExamEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<ExamEntity, Long> {

  List<ExamEntity> findByCourseId(Long courseId);
}

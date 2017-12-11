package edu.nju.onlineexam.dao;

import edu.nju.onlineexam.entity.QuestionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {

  List<QuestionEntity> findByCourseId(long courseId);

}

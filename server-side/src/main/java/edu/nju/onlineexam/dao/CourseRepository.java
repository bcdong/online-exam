package edu.nju.onlineexam.dao;

import edu.nju.onlineexam.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
}

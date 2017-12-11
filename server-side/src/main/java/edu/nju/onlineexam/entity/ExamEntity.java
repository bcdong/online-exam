/**
 * @author yyy
 * @create 2017-11-16 15:16
 */
package edu.nju.onlineexam.entity;

import java.sql.Timestamp;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "exam", schema = "online_exam", catalog = "")
public class ExamEntity {

  private long id;
  private Timestamp createTime;
  private Timestamp updateTime;
  private long courseId;
  private String title;
  private int questionCount;
  private int score;
  private Timestamp startTime;
  private Timestamp endTime;
  private String students;
  private String code;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Basic
  @Column(name = "create_time")
  @CreationTimestamp
  public Timestamp getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Timestamp createTime) {
    this.createTime = createTime;
  }

  @Basic
  @Column(name = "update_time")
  @UpdateTimestamp
  public Timestamp getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Timestamp updateTime) {
    this.updateTime = updateTime;
  }

  @Basic
  @Column(name = "course_id")
  public long getCourseId() {
    return courseId;
  }

  public void setCourseId(long courseId) {
    this.courseId = courseId;
  }

  @Basic
  @Column(name = "title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Basic
  @Column(name = "question_count")
  public int getQuestionCount() {
    return questionCount;
  }

  public void setQuestionCount(int questionCount) {
    this.questionCount = questionCount;
  }

  @Basic
  @Column(name = "score")
  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  @Basic
  @Column(name = "start_time")
  public Timestamp getStartTime() {
    return startTime;
  }

  public void setStartTime(Timestamp startTime) {
    this.startTime = startTime;
  }

  @Basic
  @Column(name = "end_time")
  public Timestamp getEndTime() {
    return endTime;
  }

  public void setEndTime(Timestamp endTime) {
    this.endTime = endTime;
  }

  @Basic
  @Column(name = "students")
  public String getStudents() {
    return students;
  }

  public void setStudents(String students) {
    this.students = students;
  }

  @Basic
  @Column(name = "code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ExamEntity that = (ExamEntity) o;

    if (id != that.id) {
      return false;
    }
    if (courseId != that.courseId) {
      return false;
    }
    if (questionCount != that.questionCount) {
      return false;
    }
    if (score != that.score) {
      return false;
    }
    if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) {
      return false;
    }
    if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) {
      return false;
    }
    if (title != null ? !title.equals(that.title) : that.title != null) {
      return false;
    }
    if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null) {
      return false;
    }
    if (endTime != null ? !endTime.equals(that.endTime) : that.endTime != null) {
      return false;
    }
    if (students != null ? !students.equals(that.students) : that.students != null) {
      return false;
    }
    if (code != null ? !code.equals(that.code) : that.code != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
    result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
    result = 31 * result + (int) (courseId ^ (courseId >>> 32));
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + questionCount;
    result = 31 * result + score;
    result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
    result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
    result = 31 * result + (students != null ? students.hashCode() : 0);
    result = 31 * result + (code != null ? code.hashCode() : 0);
    return result;
  }
}

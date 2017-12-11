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
@Table(name = "exam_score", schema = "online_exam", catalog = "")
public class ExamScoreEntity {

  private long id;
  private Timestamp createTime;
  private Timestamp updateTime;
  private long examId;
  private long studentId;
  private String questions;
  private String answers;
  private int score;
  private int status;

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
  @Column(name = "exam_id")
  public long getExamId() {
    return examId;
  }

  public void setExamId(long examId) {
    this.examId = examId;
  }

  @Basic
  @Column(name = "student_id")
  public long getStudentId() {
    return studentId;
  }

  public void setStudentId(long studentId) {
    this.studentId = studentId;
  }

  @Basic
  @Column(name = "questions")
  public String getQuestions() {
    return questions;
  }

  public void setQuestions(String questions) {
    this.questions = questions;
  }

  @Basic
  @Column(name = "answers")
  public String getAnswers() {
    return answers;
  }

  public void setAnswers(String answers) {
    this.answers = answers;
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
  @Column(name = "status")
  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ExamScoreEntity that = (ExamScoreEntity) o;

    if (id != that.id) {
      return false;
    }
    if (examId != that.examId) {
      return false;
    }
    if (studentId != that.studentId) {
      return false;
    }
    if (score != that.score) {
      return false;
    }
    if (status != that.status) {
      return false;
    }
    if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) {
      return false;
    }
    if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) {
      return false;
    }
    if (questions != null ? !questions.equals(that.questions) : that.questions != null) {
      return false;
    }
    if (answers != null ? !answers.equals(that.answers) : that.answers != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
    result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
    result = 31 * result + (int) (examId ^ (examId >>> 32));
    result = 31 * result + (int) (studentId ^ (studentId >>> 32));
    result = 31 * result + (questions != null ? questions.hashCode() : 0);
    result = 31 * result + (answers != null ? answers.hashCode() : 0);
    result = 31 * result + score;
    result = 31 * result + (int) status;
    return result;
  }
}

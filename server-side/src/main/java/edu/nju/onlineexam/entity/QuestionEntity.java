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
@Table(name = "question", schema = "online_exam", catalog = "")
public class QuestionEntity {

  private long id;
  private Timestamp createTime;
  private Timestamp updateTime;
  private long courseId;
  private int type;
  private String content;
  private String options;
  private String answer;

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
  @Column(name = "type")
  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  @Basic
  @Column(name = "content")
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Basic
  @Column(name = "options")
  public String getOptions() {
    return options;
  }

  public void setOptions(String options) {
    this.options = options;
  }

  @Basic
  @Column(name = "answer")
  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    QuestionEntity that = (QuestionEntity) o;

    if (id != that.id) {
      return false;
    }
    if (courseId != that.courseId) {
      return false;
    }
    if (type != that.type) {
      return false;
    }
    if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) {
      return false;
    }
    if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) {
      return false;
    }
    if (content != null ? !content.equals(that.content) : that.content != null) {
      return false;
    }
    if (options != null ? !options.equals(that.options) : that.options != null) {
      return false;
    }
    if (answer != null ? !answer.equals(that.answer) : that.answer != null) {
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
    result = 31 * result + (int) type;
    result = 31 * result + (content != null ? content.hashCode() : 0);
    result = 31 * result + (options != null ? options.hashCode() : 0);
    result = 31 * result + (answer != null ? answer.hashCode() : 0);
    return result;
  }
}

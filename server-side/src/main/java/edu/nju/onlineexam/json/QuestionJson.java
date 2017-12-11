/**
 * the request body of upload questions
 *
 * @author yyy
 * @create 2017-11-09 23:22
 */
package edu.nju.onlineexam.json;

import lombok.Data;
import lombok.NonNull;

@Data
public class QuestionJson {

    private int type;
    @NonNull
    private String content;
    @NonNull
    private String options;
    @NonNull
    private String answer;

}

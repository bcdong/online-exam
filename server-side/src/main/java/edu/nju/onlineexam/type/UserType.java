package edu.nju.onlineexam.type;

public enum UserType {
    STUDENT(0), TEACHER(1);

    private int code;

    UserType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

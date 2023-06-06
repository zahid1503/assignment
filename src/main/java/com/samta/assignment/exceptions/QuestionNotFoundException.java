package com.samta.assignment.exceptions;

public class QuestionNotFoundException extends  RuntimeException{
    private Integer errorCode;
    private String massage;

    public QuestionNotFoundException(ErrorCode errorCode, String massage){
        super(massage);
        this.errorCode = errorCode.getCode();
        this.massage = massage;
    }
}

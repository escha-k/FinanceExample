package com.project.finance.exception.impl;

import com.project.finance.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class PasswordNotMatchException extends AbstractException {

    @Override
    public int getStatusCode() {
        return HttpStatus.UNAUTHORIZED.value();
    }

    @Override
    public String getMessage() {
        return "비밀번호가 맞지 않습니다.";
    }
}

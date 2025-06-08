package com.project.finance.exception.impl;

import com.project.finance.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AbstractException {

    @Override
    public int getStatusCode() {
        return HttpStatus.UNAUTHORIZED.value();
    }

    @Override
    public String getMessage() {
        return "등록되지 않은 사용자명입니다.";
    }
}

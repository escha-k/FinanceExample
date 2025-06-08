package com.project.finance.exception.impl;

import com.project.finance.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class EmptyTickerException extends AbstractException {

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "빈 티커입니다.";
    }
}

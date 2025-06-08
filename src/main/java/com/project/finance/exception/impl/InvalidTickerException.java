package com.project.finance.exception.impl;

import com.project.finance.exception.AbstractException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class InvalidTickerException extends AbstractException {

    private final String ticker;

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "서버에 저장되지 않은 티커입니다: " + ticker;
    }
}

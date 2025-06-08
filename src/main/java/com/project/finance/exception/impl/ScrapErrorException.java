package com.project.finance.exception.impl;

import com.project.finance.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class ScrapErrorException extends AbstractException {

    @Override
    public int getStatusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    @Override
    public String getMessage() {
        return "스크랩 도중에 오류가 발생했습니다.";
    }
}

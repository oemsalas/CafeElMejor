package com.cafe.cafeDemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -5986003061545677254L;

	public ResourceNotFoundException(String message) {
        super(message);
    }
}

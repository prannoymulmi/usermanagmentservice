package com.prannoy.usermanagementservice.rest.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class ErrorDTO {
    private String errorTxt;
    private HttpStatus httpStatus;

    public ErrorDTO(String errorTxt, HttpStatus httpStatus) {
        this.errorTxt = errorTxt;
        this.httpStatus = httpStatus;
    }

    public ResponseEntity<ErrorDTO> createResponse() {
        return ResponseEntity.status(httpStatus).body(this);
    }
}

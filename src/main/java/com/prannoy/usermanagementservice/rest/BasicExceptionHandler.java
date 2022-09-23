package com.prannoy.usermanagementservice.rest;

import com.prannoy.usermanagementservice.config.EnableBasicErrorHandlingConfig;
import com.prannoy.usermanagementservice.rest.dto.ErrorDTO;
import com.prannoy.usermanagementservice.rest.exception.BadRequestException;
import com.prannoy.usermanagementservice.rest.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@ControllerAdvice(annotations = EnableBasicErrorHandlingConfig.class)
public class BasicExceptionHandler {

    /**
     * If un expected Errors occur, it will be handled here
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public ResponseEntity<ErrorDTO> generalError(Exception ex) {
        log.trace(ex.toString(), ex);
        return new ErrorDTO(ex.toString(), HttpStatus.INTERNAL_SERVER_ERROR).createResponse();
    }


    @ExceptionHandler(value = {BadRequestException.class})
    @ResponseBody
    public ResponseEntity<ErrorDTO> badRequest(BadRequestException ex) {
        log.trace(ex.getMessage(), ex);
        return new ErrorDTO(ex.getMessage(), HttpStatus.BAD_REQUEST).createResponse();
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    @ResponseBody
    public ResponseEntity<ErrorDTO> badRequest(ResourceNotFoundException ex) {
        log.trace(ex.getMessage(), ex);
        return new ErrorDTO(ex.getMessage(), HttpStatus.NOT_FOUND).createResponse();
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseBody
    public ResponseEntity<ErrorDTO> validationError(MethodArgumentNotValidException ex) {
        final var fieldErrors = ex.getBindingResult().getFieldErrors();

        final var fieldErrorDefaultMessage = getFieldErrorDefaultMessage(fieldErrors);
        log.trace(ex.getMessage(), ex);
        return new ErrorDTO(fieldErrorDefaultMessage, HttpStatus.BAD_REQUEST).createResponse();
    }

    /**
     * Get the default message from validation errors
     * @param fieldErrors
     * @return
     */
    private String getFieldErrorDefaultMessage(List<FieldError> fieldErrors) {
        String message= "Bad Input with some errors";
        for (FieldError fieldError: fieldErrors) {
            message =  fieldError.getDefaultMessage();
        }
        return message;
    }

}

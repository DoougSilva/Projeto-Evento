package com.projuris.projetoStag.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;


@ControllerAdvice
public class ExceptionTranslator {

    @ExceptionHandler({ValidEventException.class})
    public ResponseEntity<EventoProblem> validationexeption(ValidEventException exception, NativeWebRequest request){
        EventoProblem problem = EventoProblem.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(exception.getMessage())
                .title("ValidEventException")
                .build();
        return new ResponseEntity<>(problem, HttpStatus.CONFLICT);
    }
}

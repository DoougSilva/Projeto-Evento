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
                .status(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .title("ValidEventException")
                .build();
        return new ResponseEntity<>(problem, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ExistsEventoException.class})
    public ResponseEntity<EventoProblem> validationexeption(ExistsEventoException exception, NativeWebRequest request){
        EventoProblem problem = EventoProblem.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .title("ExistsEventoException")
                .build();
        return new ResponseEntity<>(problem, HttpStatus.NOT_FOUND);
    }
}

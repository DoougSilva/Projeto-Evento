package com.projuris.projetoStag.exception;

import org.springframework.beans.factory.parsing.Problem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionTranslator {

    @ExceptionHandler({ValidEventException.class})
    public final ResponseEntity<Problem> validationexeption(ValidEventException e){
    }
}

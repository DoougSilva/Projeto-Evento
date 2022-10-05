package com.projuris.projetoStag.exception;

import com.projuris.projetoStag.specification.ValidationMessage;

import java.util.Set;
import java.util.stream.Collectors;

public class ValidEventException extends javax.validation.ValidationException{

    private Set<ValidationMessage> validationMessages;
    private Boolean hasError = false;
    public ValidEventException(Set<ValidationMessage> validationMessages) {
        super((String)validationMessages.stream().map(ValidationMessage::getMessage).collect(Collectors.joining(", ")));
        this.validationMessages = validationMessages;
    }

    public Set<ValidationMessage> getValidationMessages() {
        return this.validationMessages;
    }

    public Boolean hasError() {
        return this.hasError;
    }

    public ValidEventException() {
    }

    public ValidEventException(String message) {
        super(message);
        this.hasError = true;
    }

    public ValidEventException(Throwable cause) {
        super(cause);
    }

    public ValidEventException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.projuris.projetoStag.specification;

public class ValidationMessage {
    private final String message;
    private final String field;

    public ValidationMessage(String message, String field) {
        this.message = message;
        this.field = field;
    }
    
    public String getMessage() {
        return this.message;
    }
    
}

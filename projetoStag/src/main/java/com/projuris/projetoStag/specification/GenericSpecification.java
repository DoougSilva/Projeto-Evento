package com.projuris.projetoStag.specification;

import com.projuris.projetoStag.exception.ValidEventException;

public abstract class GenericSpecification<T> implements Specification<T> {

    private String message;
    private String field;

    public GenericSpecification(){}

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessage(String field, String message) {
        this.message = message;
        this.field = field;
    }

    public String getMessage() {
        return this.message;
    }

    public String getField() {
        return this.field;
    }

    public GenericSpecification<T> and(GenericSpecification<T> other) {
        return new AndSpecification(this, other);
    }

    public boolean isSatisfiedByWithException(T candidate) throws ValidEventException {
        boolean result = this.isSatisfiedBy(candidate);
        if (!result){
            throw new ValidEventException(this.message);
        } else {
            return true;
        }
    }

    public ValidEventException isSatisfiedByWithoutExceptionIfNecessary(T candidate, Boolean shouldBeWithoutException) throws ValidEventException {
        boolean result = this.isSatisfiedBy(candidate);
        if (!result) {
            ValidEventException newValidateEventExeption = new ValidEventException(this.message);
            if (shouldBeWithoutException){
                return newValidateEventExeption;
            } else {
                throw newValidateEventExeption;
            }
        } else {
            return null;
        }
    }
}

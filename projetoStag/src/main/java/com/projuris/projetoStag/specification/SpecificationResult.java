package com.projuris.projetoStag.specification;

import com.projuris.projetoStag.exception.ValidEventException;

import java.util.HashSet;
import java.util.Set;

public class SpecificationResult<T> {
    private Set<ValidationMessage> messages = new HashSet();

    public SpecificationResult() {
    }

    public void add(ValidationMessage validationMessage) {
        this.messages.add(validationMessage);
    }

    public void throwMessages() throws ValidEventException {
        if (!this.messages.isEmpty()) {
            throw new ValidEventException(this.messages);
        }
    }
}

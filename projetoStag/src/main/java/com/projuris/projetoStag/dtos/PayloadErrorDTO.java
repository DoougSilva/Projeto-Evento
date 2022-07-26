package com.projuris.projetoStag.dtos;

import lombok.Getter;

@Getter
public class PayloadErrorDTO {

    private final String messageError;

    public PayloadErrorDTO(String message) {
        this.messageError = message;
    }
}

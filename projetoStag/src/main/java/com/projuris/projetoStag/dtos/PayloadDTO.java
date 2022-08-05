package com.projuris.projetoStag.dtos;

import lombok.Getter;

@Getter
public class PayloadDTO {

    private final String message;

    public PayloadDTO(String message) {
        this.message= message;
    }
}

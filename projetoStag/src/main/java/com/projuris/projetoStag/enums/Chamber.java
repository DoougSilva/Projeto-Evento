package com.projuris.projetoStag.enums;

import com.projuris.projetoStag.exception.ValidEventException;
import lombok.Getter;

@Getter
public enum Chamber {
    SALA_1(1),
    SALA_2(2),
    SALA_3(3),
    SALA_4(4),
    SALA_5(5);

    private Integer code;

    private Chamber(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static Chamber valueOf(Integer code) {
        for (Chamber value : Chamber.values()) {
            if (code == value.getCode()) {
                return value;
            }
        }
        throw new IllegalArgumentException("Chamber Invalid!");
    }

}

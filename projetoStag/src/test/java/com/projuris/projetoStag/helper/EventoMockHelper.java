package com.projuris.projetoStag.helper;

import com.projuris.projetoStag.dtos.EventoDTO;
import com.projuris.projetoStag.enums.Chamber;
import com.projuris.projetoStag.entities.Evento;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class EventoMockHelper {

    public Evento createEvento(){
        Evento evento = new Evento();
        evento.setId(1L);
        evento.setName("Test");
        evento.setDate(LocalDateTime.of(2022, 7, 28, 8, 00));
        evento.setDateFinal(LocalDateTime.of(2022, 7, 28, 8, 01));
        evento.setChamber(Chamber.SALA_1);

        return evento;
    }

    public EventoDTO createEventoDTO(){
        EventoDTO eventoDTO = new EventoDTO();
        eventoDTO.setName("Test");
        eventoDTO.setDate(LocalDateTime.of(2022, 7, 28, 8, 02));
        eventoDTO.setDateFinal(LocalDateTime.of(2022, 7, 28, 8, 03));
        eventoDTO.setChamber(Chamber.SALA_1);
        return eventoDTO;
    }
}

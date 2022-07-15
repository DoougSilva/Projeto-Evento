package com.projuris.projetoStag.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projuris.projetoStag.entities.Chamber;
import com.projuris.projetoStag.entities.Evento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventoDTO {

    private Long id;

    private String name;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime date;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime dateFinal;

    @Enumerated(EnumType.STRING)
    private Chamber chamber;

    public EventoDTO(Evento evento) {
        this.id = evento.getId();
        this.name = evento.getName();
        this.date = evento.getDate();
        this.dateFinal = evento.getDateFinal();
        this.chamber = evento.getChamber();
    }
    public EventoDTO(Optional<Evento> eventoOptional) {
        this.id = eventoOptional.get().getId();
        this.name = eventoOptional.get().getName();
        this.date = eventoOptional.get().getDate();
        this.dateFinal = eventoOptional.get().getDateFinal();
        this.chamber = eventoOptional.get().getChamber();
    }
}

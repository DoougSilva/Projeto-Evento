package com.projuris.projetoStag.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "eventos")
public class Evento implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime date;

    @Column
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime dateFinal;

    @Enumerated(EnumType.STRING)
    private Chamber chamber;

    public Evento(Optional<Evento> byId) {
        this.id = byId.get().getId();
        this.name = byId.get().getName();
        this.date = byId.get().getDate();
        this.dateFinal = byId.get().getDateFinal();
        this.chamber = byId.get().getChamber();
    }
}
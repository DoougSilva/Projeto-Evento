package com.projuris.projetoStag.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @NotNull
    @Column
    private String name;

    @NotNull
    @Column
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime date;

    @NotNull
    @Column
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime dateFinal;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Chamber chamber;

    public Evento(Optional<Evento> byId) {
        this.id = byId.get().getId();
        this.name = byId.get().getName();
        this.date = byId.get().getDate();
        this.dateFinal = byId.get().getDateFinal();
        this.chamber = byId.get().getChamber();
    }
    public Evento(Long id){
        this.id = id;
    }
}
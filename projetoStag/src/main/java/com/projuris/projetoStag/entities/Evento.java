package com.projuris.projetoStag.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projuris.projetoStag.enums.Chamber;
import com.projuris.projetoStag.exception.ValidEventException;
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
@Builder
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
    private Integer chamber;

    public Evento(String name, LocalDateTime date, LocalDateTime dateFinal, Chamber chamber) {
        this.name = name;
        this.date = date;
        this.dateFinal = dateFinal;
        setChamber(chamber);
    }

    public Chamber getChamber() {
        return Chamber.valueOf(chamber);
    }

    public void setChamber(Chamber chamber) {
        this.chamber = chamber.getCode();
    }
}
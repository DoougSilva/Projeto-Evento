package com.projuris.projetoStag.repositories;

import com.projuris.projetoStag.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

}
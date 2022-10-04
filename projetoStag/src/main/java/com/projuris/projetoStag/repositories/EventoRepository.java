package com.projuris.projetoStag.repositories;

import com.projuris.projetoStag.entities.Evento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    Page<Evento> findAllByChamber(Integer chamberCode, PageRequest pageRequest);

    Optional<Evento> findByNameIgnoreCase(String name);
}
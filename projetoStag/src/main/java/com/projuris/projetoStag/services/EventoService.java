package com.projuris.projetoStag.services;

import com.projuris.projetoStag.dtos.EventoDTO;
import com.projuris.projetoStag.entities.Chamber;
import com.projuris.projetoStag.entities.Evento;
import com.projuris.projetoStag.repositories.EventoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static java.time.ZoneId.systemDefault;


@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;


    @Transactional
    public Evento save(EventoDTO eventoDTO) {
        var evento = new Evento();
        BeanUtils.copyProperties(eventoDTO, evento);
        return eventoRepository.save(evento);
    }

    @Transactional
    public Page<EventoDTO> findAll(PageRequest pageRequest) {
        Page<Evento> list = eventoRepository.findAll(pageRequest);
        return list.map(EventoDTO::new);
    }

    public boolean existsEvento(LocalDateTime date, LocalDateTime dateFinal, Chamber chamber) {
        ZoneId ZoneId = systemDefault();
        long miliDate = date.atZone(ZoneId).toEpochSecond();
        long miliDateFinal = dateFinal.atZone(ZoneId).toEpochSecond();
        List<Evento> eventos = eventoRepository.findAll();
        boolean resp;
        for (Evento e : eventos) {
            long miliTest =  e.getDate().atZone(ZoneId).toEpochSecond();
            long miliTest1 = e.getDateFinal().atZone(ZoneId).toEpochSecond();
            if (miliDate > miliTest && miliDate < miliTest1 && e.getChamber() == chamber ) {
                return false;
            }
            if (miliDateFinal > miliTest && miliDateFinal <= miliTest1 && e.getChamber() == chamber ) {
                return false;
            }
            if (miliDate < miliTest && miliDate < miliTest1 && miliDateFinal > miliTest && miliDateFinal > miliTest1 && e.getChamber() == chamber){
                return false;
            }
            if ( miliDate == miliTest && miliDateFinal == miliTest1 && e.getChamber() == chamber){
                return false;
            }
        }
        return true;
    }

    public boolean checkDate(LocalDateTime date, LocalDateTime dateFinal){
        ZoneId ZoneId = systemDefault();
        long milinow = LocalDateTime.now().atZone(ZoneId).toEpochSecond();
        long miliDate = date.atZone(ZoneId).toEpochSecond();
        long miliDateFinal = dateFinal.atZone(ZoneId).toEpochSecond();
        if(miliDate < milinow || miliDateFinal <= miliDate ){
            return false;
        }
        else return true;
    }

    public Optional<EventoDTO> findById(Long id){
        Optional<Evento> eventoOptional = eventoRepository.findById(id);
        Optional<EventoDTO> eventoDTOOptional = converterEmDTO(eventoOptional);
        return eventoDTOOptional;
        }

    @Transactional
    public void delete(EventoDTO eventoDTO){
        var evento = new Evento();
        BeanUtils.copyProperties(eventoDTO, evento);
        eventoRepository.delete(evento);
    }

    public Optional<EventoDTO> converterEmDTO(Optional<Evento> eventoOptional){
        var eventoDTO = new EventoDTO(eventoOptional);
        Optional<EventoDTO> eventoDTOOptional = Optional.of(eventoDTO);
        return eventoDTOOptional;
    }
}
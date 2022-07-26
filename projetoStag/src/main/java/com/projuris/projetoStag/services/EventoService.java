package com.projuris.projetoStag.services;

import com.projuris.projetoStag.dtos.EventoDTO;
import com.projuris.projetoStag.entities.Evento;
import com.projuris.projetoStag.repositories.EventoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static java.time.ZoneId.systemDefault;


@Transactional
@Service
public class EventoService {


    private final EventoRepository eventoRepository;

    private final ModelMapper modelMapper;

    public EventoService(EventoRepository eventoRepository, ModelMapper modelMapper) {
        this.eventoRepository = eventoRepository;
        this.modelMapper = modelMapper;
    }


    public ResponseEntity<Object> save(EventoDTO eventoDTO) {
        if(!existsEvento(eventoDTO)){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Chamber used in Data.");
        }
        if(!checkDate(eventoDTO.getDate(), eventoDTO.getDateFinal())){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Date invalid.");
        }
        var evento = new Evento();
        BeanUtils.copyProperties(eventoDTO, evento);
        eventoRepository.save(evento);
        eventoDTO.setId(evento.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoDTO);
    }

    @Transactional(readOnly = true)
    public Page<EventoDTO> findAll(PageRequest pageRequest) {
        Page<Evento> list = eventoRepository.findAll(pageRequest);
        return list.map(EventoDTO::new);
    }

    public ResponseEntity<Object> updateEvento(Long id, EventoDTO eventoDTO){
        Optional<Evento> eventoOptional = eventoRepository.findById(id);
        if(eventoOptional.isEmpty()){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento with id " + id + " does not exists.");
        }
        var evento = new Evento(eventoOptional);
        eventoDTO.setId(id);
        if (eventoDTO.getName().length() > 2){
            evento.setName(eventoDTO.getName());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Name invalid.");
        }
        if(!existsEvento(eventoDTO)){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Chamber used in Data.");
        }
        if(!checkDate(eventoDTO.getDate(), eventoDTO.getDateFinal())){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Date invalid.");
        }
        evento.setDate(eventoDTO.getDate());
        evento.setDateFinal(eventoDTO.getDateFinal());
        evento.setChamber(eventoDTO.getChamber());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(toEventoDTO(evento));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> findByEventoId(Long id){
        if(!eventoRepository.existsById(id)){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento with id " + id + " does not exists.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(eventoRepository.findById(id).map(this::toEventoDTO));
        }


    public ResponseEntity<Object> delete(Long id){
        if(!eventoRepository.existsById(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento with id " + id + " does not exists.");
        }
        eventoRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Evento deleted successfully.");
    }

    public boolean existsEvento(EventoDTO eventoDTO) {
        ZoneId ZoneId = systemDefault();
        long miliDate = eventoDTO.getDate().atZone(ZoneId).toEpochSecond();
        long miliDateFinal = eventoDTO.getDateFinal().atZone(ZoneId).toEpochSecond();
        List<Evento> eventos = eventoRepository.findAll();
        if(eventoDTO.getId() != null) {
            eventos.remove(new Evento(eventoRepository.findById(eventoDTO.getId())));
        }
        for (Evento e : eventos) {
            long miliTest =  e.getDate().atZone(ZoneId).toEpochSecond();
            long miliTest1 = e.getDateFinal().atZone(ZoneId).toEpochSecond();
            if (miliDate > miliTest && miliDate < miliTest1 && e.getChamber() == eventoDTO.getChamber() ) {
                return false;
            }
            if (miliDateFinal > miliTest && miliDateFinal <= miliTest1 && e.getChamber() == eventoDTO.getChamber() ) {
                return false;
            }
            if (miliDate < miliTest && miliDate < miliTest1 && miliDateFinal > miliTest && miliDateFinal > miliTest1 && e.getChamber() == eventoDTO.getChamber()){
                return false;
            }
            if ( miliDate == miliTest && miliDateFinal == miliTest1 && e.getChamber() == eventoDTO.getChamber()){
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

    private EventoDTO toEventoDTO(Evento evento){
        return modelMapper.map(evento, EventoDTO.class);
    }
}
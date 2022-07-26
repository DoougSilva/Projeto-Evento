package com.projuris.projetoStag.services;

import com.projuris.projetoStag.dtos.EventoDTO;
import com.projuris.projetoStag.entities.Evento;
import com.projuris.projetoStag.repositories.EventoRepository;
import com.projuris.projetoStag.utils.Payload;
import lombok.RequiredArgsConstructor;
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
import java.util.List;
import java.util.Optional;


@Transactional
@RequiredArgsConstructor
@Service
public class EventoService {


    private final EventoRepository eventoRepository;

    private final ModelMapper modelMapper;

    private final Clock clock;


    public ResponseEntity<Object> save(EventoDTO eventoDTO) {
        if (eventoDTO.getName().length() < 2){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Payload("Name invalid.").toJson());
        }
        if(existsEvento(eventoDTO)){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Payload("Chamber used in Data.").toJson());
        }
        if(checkDate(eventoDTO.getDate(), eventoDTO.getDateFinal())){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Payload("Date invalid.").toJson());
        }
        var evento = new Evento();
        BeanUtils.copyProperties(eventoDTO, evento);
        eventoRepository.save(evento);
        Evento rest = eventoRepository.findById(evento.getId()).orElseThrow();
        return ResponseEntity.status(HttpStatus.CREATED).body(toEventoDTO(rest));
    }

    @Transactional(readOnly = true)
    public Page<EventoDTO> findAll(PageRequest pageRequest) {
        Page<Evento> list = eventoRepository.findAll(pageRequest);
        return list.map(EventoDTO::new);
    }

    public ResponseEntity<Object> updateEvento(Long id, EventoDTO eventoDTO){
        Optional<Evento> eventoOptional = eventoRepository.findById(id);
        if(eventoOptional.isEmpty()){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Payload("Evento with id " + id + " does not exists.").toJson());
        }
        if (eventoDTO.getName().length() > 2){
            eventoOptional.get().setName(eventoDTO.getName());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Payload("Name invalid.").toJson());
        }
        eventoDTO.setId(id);
        if(existsEvento(eventoDTO)){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Payload("Chamber used in Data.").toJson());
        }
        if(checkDate(eventoDTO.getDate(), eventoDTO.getDateFinal())){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Payload("Date invalid.").toJson());
        }
        eventoOptional.get().setDate(eventoDTO.getDate());
        eventoOptional.get().setDateFinal(eventoDTO.getDateFinal());
        eventoOptional.get().setChamber(eventoDTO.getChamber());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(toEventoDTO(eventoOptional.get()));
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Payload("Evento with id " + id + " does not exists.").toJson());
        }
        eventoRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new Payload("Evento deleted successfully.").toJson());
    }

    public boolean existsEvento(EventoDTO eventoDTO) {
        List<Evento> list = eventoRepository.findAll();
        if(eventoDTO.getId() != null){
            Optional<Evento> eventoOptional = eventoRepository.findById(eventoDTO.getId());
            list.remove(eventoOptional.get());
        }
        for(Evento evento : list){
            if(eventoDTO.getDate().isAfter(evento.getDate())
                    && eventoDTO.getDate().isBefore(evento.getDateFinal())
                    && eventoDTO.getChamber() == evento.getChamber()){
               return true;
            }
            if(eventoDTO.getDateFinal().isAfter(evento.getDate())
                    && eventoDTO.getDateFinal().isBefore(evento.getDateFinal())
                    && eventoDTO.getChamber() == evento.getChamber()){
                return true;
            }
            if(eventoDTO.getDate().isEqual(evento.getDate())
                    && eventoDTO.getDateFinal().isEqual(evento.getDateFinal())
                    && eventoDTO.getChamber() == evento.getChamber()){
                return true;
            }
            if(evento.getDate().isAfter(eventoDTO.getDate())
                    && evento.getDate().isBefore(eventoDTO.getDateFinal())
                    && eventoDTO.getChamber() == evento.getChamber()){
                return true;
            }
            if(evento.getDateFinal().isAfter(eventoDTO.getDate())
                    && evento.getDateFinal().isBefore(eventoDTO.getDateFinal())
                    && eventoDTO.getChamber() == evento.getChamber()){
                return true;
            }
        }
        return false;
    }

    public boolean checkDate(LocalDateTime date, LocalDateTime dateFinal){
        if(dateFinal.isBefore(date)){
            return true;
        }
        if(date.isBefore(LocalDateTime.now(clock))){
            return true;
        }
        return false;
    }

    private EventoDTO toEventoDTO(Evento evento){
        return modelMapper.map(evento, EventoDTO.class);
    }
}
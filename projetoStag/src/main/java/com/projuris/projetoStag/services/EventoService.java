package com.projuris.projetoStag.services;

import com.projuris.projetoStag.dtos.EventoDTO;
import com.projuris.projetoStag.entities.Evento;
import com.projuris.projetoStag.repositories.EventoRepository;
import com.projuris.projetoStag.dtos.PayloadErrorDTO;
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


    public ResponseEntity<Object> saveEvento(EventoDTO eventoDTO) {
        if (eventoDTO.getName().length() < 2){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PayloadErrorDTO("Name invalid."));
        }
        if(existsEvento(eventoDTO)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PayloadErrorDTO("Chamber used in Data."));
        }
        if(checkDate(eventoDTO)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PayloadErrorDTO("Date invalid."));
        }
        var evento = new Evento();
        BeanUtils.copyProperties(eventoDTO, evento);
        eventoRepository.save(evento);
        Optional<Evento> rest = eventoRepository.findById(evento.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(toEventoDTO(rest.get()));
    }

    @Transactional(readOnly = true)
    public Page<EventoDTO> findAll(PageRequest pageRequest) {
        Page<Evento> list = eventoRepository.findAll(pageRequest);
        return list.map(EventoDTO::new);
    }

    public ResponseEntity<Object> updateEvento(Long id, EventoDTO eventoDTO){
        Optional<Evento> eventoOptional = eventoRepository.findById(id);
        if(eventoOptional.isEmpty()){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PayloadErrorDTO("Evento with id " + id + " does not exists."));
        }
        if (eventoDTO.getName().length() > 2){
            eventoOptional.get().setName(eventoDTO.getName());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new PayloadErrorDTO("Name invalid."));
        }
        eventoDTO.setId(id);
        if(existsEvento(eventoDTO)){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new PayloadErrorDTO("Chamber used in Data."));
        }
        if(checkDate(eventoDTO)){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new PayloadErrorDTO("Date invalid."));
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PayloadErrorDTO("Evento with id " + id + " does not exists."));
        }
        eventoRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new PayloadErrorDTO("Evento deleted successfully."));
    }

    private boolean existsEvento(EventoDTO eventoDTO) {
        List<Evento> list = eventoRepository.findAll();
        for(Evento evento : list){
            if(eventoDTO.getDate().isAfter(evento.getDate())
                    && eventoDTO.getDate().isBefore(evento.getDateFinal())
                    && eventoDTO.getChamber() == evento.getChamber()
                    && eventoDTO.getId() != evento.getId()){
               return true;
            }
            if(eventoDTO.getDateFinal().isAfter(evento.getDate())
                    && eventoDTO.getDateFinal().isBefore(evento.getDateFinal())
                    && eventoDTO.getChamber() == evento.getChamber()
                    && eventoDTO.getId() != evento.getId()){
                return true;
            }
            if(eventoDTO.getDate().isEqual(evento.getDate())
                    && eventoDTO.getDateFinal().isEqual(evento.getDateFinal())
                    && eventoDTO.getChamber() == evento.getChamber()
                    && eventoDTO.getId() != evento.getId()){
                return true;
            }
            if(evento.getDate().isAfter(eventoDTO.getDate())
                    && evento.getDate().isBefore(eventoDTO.getDateFinal())
                    && eventoDTO.getChamber() == evento.getChamber()
                    && eventoDTO.getId() != evento.getId()){
                return true;
            }
            if(evento.getDateFinal().isAfter(eventoDTO.getDate())
                    && evento.getDateFinal().isBefore(eventoDTO.getDateFinal())
                    && eventoDTO.getChamber() == evento.getChamber()
                    && eventoDTO.getId() != evento.getId()){
                return true;
            }
        }
        return false;
    }

    private boolean checkDate(EventoDTO eventoDTO){
        if(eventoDTO.getDateFinal().isBefore(eventoDTO.getDate())){
            return true;
        }
        if(eventoDTO.getDate().isBefore(LocalDateTime.now(clock))){
            return true;
        }
        if (eventoDTO.getDate().isEqual(eventoDTO.getDateFinal())){
            return true;
        }
        return false;
    }

    private EventoDTO toEventoDTO(Evento evento){
        return modelMapper.map(evento, EventoDTO.class);
    }
}
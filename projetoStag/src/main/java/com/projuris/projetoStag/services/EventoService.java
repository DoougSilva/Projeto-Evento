package com.projuris.projetoStag.services;

import com.projuris.projetoStag.dtos.EventoDTO;
import com.projuris.projetoStag.entities.Evento;
import com.projuris.projetoStag.exception.ValidEventException;
import com.projuris.projetoStag.repositories.EventoRepository;
import com.projuris.projetoStag.dtos.PayloadErrorDTO;
import com.projuris.projetoStag.validation.EventoValidation;
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
import java.util.Optional;


@Transactional
@RequiredArgsConstructor
@Service
public class EventoService {


    private final EventoRepository eventoRepository;

    private final ModelMapper modelMapper;

    private final Clock clock;


    public EventoDTO saveEvento(EventoDTO eventoDTO) throws ValidEventException {
        EventoValidation.validaEvento(eventoDTO, this.eventoRepository, this.clock);
        var evento = new Evento();
        BeanUtils.copyProperties(eventoDTO, evento);
        eventoRepository.save(evento);
        Optional<Evento> rest = eventoRepository.findById(evento.getId());
        return toEventoDTO(rest.get());
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> findAll(PageRequest pageRequest) {
        Page<Evento> list = eventoRepository.findAll(pageRequest);
        return ResponseEntity.status(HttpStatus.OK).body(list.map(EventoDTO::new));
    }

    public EventoDTO updateEvento(Long id, EventoDTO eventoDTO) throws ValidEventException {
        Optional<Evento> eventoOptional = eventoRepository.findById(id);
//        if(eventoOptional.isEmpty()){
//           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PayloadErrorDTO("Evento with id " + id + " does not exists."));
//        }
        eventoDTO.setId(id);
        EventoValidation.validaEvento(eventoDTO, this.eventoRepository, this.clock);
        eventoOptional.get().setName(eventoDTO.getName());
        eventoOptional.get().setDate(eventoDTO.getDate());
        eventoOptional.get().setDateFinal(eventoDTO.getDateFinal());
        eventoOptional.get().setChamber(eventoDTO.getChamber());
        return toEventoDTO(eventoOptional.get());
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

    private EventoDTO toEventoDTO(Evento evento){
        return modelMapper.map(evento, EventoDTO.class);
    }
}
package com.projuris.projetoStag.services;

import com.projuris.projetoStag.dtos.EventoDTO;
import com.projuris.projetoStag.entities.Evento;
import com.projuris.projetoStag.exception.ExistsEventoException;
import com.projuris.projetoStag.exception.ValidEventException;
import com.projuris.projetoStag.repositories.EventoRepository;
import com.projuris.projetoStag.validation.EventoValidation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public Page<Object> findAll(PageRequest pageRequest) {
        Page<Evento> list = eventoRepository.findAll(pageRequest);
        return list.map(EventoDTO::new);
    }

    public EventoDTO updateEvento(Long id, EventoDTO eventoDTO) throws ExistsEventoException, ValidEventException {
        Evento evento = EventoValidation.existsEvento(id, this.eventoRepository);
        EventoValidation.validaEvento(eventoDTO, this.eventoRepository, this.clock);
        evento.setName(eventoDTO.getName());
        evento.setDate(eventoDTO.getDate());
        evento.setDateFinal(eventoDTO.getDateFinal());
        evento.setChamber(eventoDTO.getChamber());
        return toEventoDTO(evento);
    }

    @Transactional(readOnly = true)
    public EventoDTO findByEventoId(Long id) throws ExistsEventoException {
        Evento evento = EventoValidation.existsEvento(id, this.eventoRepository);
        return toEventoDTO(evento);
        }


    public String delete(Long id) throws ExistsEventoException {
        Evento evento = EventoValidation.existsEvento(id, this.eventoRepository);
        eventoRepository.deleteById(id);
        return "Evento deleted successfully.";
    }

    private EventoDTO toEventoDTO(Evento evento){
        return modelMapper.map(evento, EventoDTO.class);
    }
}
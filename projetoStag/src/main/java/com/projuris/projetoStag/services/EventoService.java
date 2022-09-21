package com.projuris.projetoStag.services;

import com.projuris.projetoStag.dtos.EventoDTO;
import com.projuris.projetoStag.dtos.PayloadDTO;
import com.projuris.projetoStag.entities.Evento;
import com.projuris.projetoStag.exception.ExistsEventoException;
import com.projuris.projetoStag.exception.ValidEventException;
import com.projuris.projetoStag.repositories.EventoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    private String message;

    public EventoDTO saveEvento(EventoDTO eventoDTO) throws ValidEventException {
        validaEvento(eventoDTO);
        var evento = new Evento();
        BeanUtils.copyProperties(eventoDTO, evento);
        eventoRepository.save(evento);
        return toEventoDTO(evento);
    }

    @Transactional(readOnly = true)
    public Page<Object> findAll(PageRequest pageRequest) {
        Page<Evento> list = eventoRepository.findAll(pageRequest);
        return list.map(EventoDTO::new);
    }

    public EventoDTO updateEvento(Long id, EventoDTO eventoDTO) throws ExistsEventoException, ValidEventException {
        Evento evento = checkEvento(id);
        eventoDTO.setId(evento.getId());
        validaEvento(eventoDTO);
        evento.setName(eventoDTO.getName());
        evento.setDate(eventoDTO.getDate());
        evento.setDateFinal(eventoDTO.getDateFinal());
        evento.setChamber(eventoDTO.getChamber());
        return toEventoDTO(evento);
    }

    @Transactional(readOnly = true)
    public EventoDTO findByEventoId(Long id) throws ExistsEventoException {
        Evento evento = checkEvento(id);
        return toEventoDTO(evento);
        }


    public Object delete(Long id) throws ExistsEventoException {
        Evento evento = checkEvento(id);
        eventoRepository.delete(evento);
        return new PayloadDTO("Evento deleted successfully!");
    }

    private EventoDTO toEventoDTO(Evento evento){
        return modelMapper.map(evento, EventoDTO.class);
    }

    public boolean validaEvento(EventoDTO eventoDTO) throws ValidEventException {
        if (existsEvento(eventoDTO) || checkDate(eventoDTO) || validName(eventoDTO.getName())) {
            throw new ValidEventException(message);
        }
        return true;
    }

    private boolean existsEvento(EventoDTO eventoDTO) {
        List<Evento> list = eventoRepository.findAll();
        for(Evento evento : list){
            if(eventoDTO.getDate().isAfter(evento.getDate())
                    && eventoDTO.getDate().isBefore(evento.getDateFinal())
                    && eventoDTO.getChamber() == evento.getChamber()
                    && eventoDTO.getId() != evento.getId()){
                message = ("Chamber used in Data!s");
                return true;
            }
            if(eventoDTO.getDateFinal().isAfter(evento.getDate())
                    && eventoDTO.getDateFinal().isBefore(evento.getDateFinal())
                    && eventoDTO.getChamber() == evento.getChamber()
                    && eventoDTO.getId() != evento.getId()){
                message = ("Chamber used in Data!");
                return true;
            }
            if(eventoDTO.getDate().isEqual(evento.getDate())
                    && eventoDTO.getDateFinal().isEqual(evento.getDateFinal())
                    && eventoDTO.getChamber() == evento.getChamber()
                    && eventoDTO.getId() != evento.getId()){
                message = ("Chamber used in Data!");
                return true;
            }
            if(evento.getDate().isAfter(eventoDTO.getDate())
                    && evento.getDate().isBefore(eventoDTO.getDateFinal())
                    && eventoDTO.getChamber() == evento.getChamber()
                    && eventoDTO.getId() != evento.getId()){
                message = ("Chamber used in Data!");
                return true;
            }
            if(evento.getDateFinal().isAfter(eventoDTO.getDate())
                    && evento.getDateFinal().isBefore(eventoDTO.getDateFinal())
                    && eventoDTO.getChamber() == evento.getChamber()
                    && eventoDTO.getId() != evento.getId()){
                message = ("Chamber used in Data!");
                return true;
            }
        }
        return false;
    }

    private boolean checkDate(EventoDTO eventoDTO){
        if(eventoDTO.getDateFinal().isBefore(eventoDTO.getDate())){
            message = ("Date invalid!");
            return true;
        }
        if(eventoDTO.getDate().isBefore(LocalDateTime.now(clock))){
            message = ("Date invalid!");
            return true;
        }
        if (eventoDTO.getDate().isEqual(eventoDTO.getDateFinal())){
            message = ("Date invalid!");
            return true;
        }
        if(eventoDTO.getDateFinal().getHour() - eventoDTO.getDate().getHour() >= 6) {
            message = ("Exceeded timeout!");
            return true;
        }
        if(eventoDTO.getDate().getMonth() != eventoDTO.getDateFinal().getMonth()
                || eventoDTO.getDate().getYear() != eventoDTO.getDateFinal().getYear()
                || eventoDTO.getDate().getDayOfMonth() != eventoDTO.getDateFinal().getDayOfMonth()){
            message = ("Exceeded timeout!");
            return true;
        }
        return false;
    }

    private boolean validName (String name){
        if (name.length() <= 2) {
            message = ("Name invalid!");
            return true;
        }
        return false;
    }

    public Evento checkEvento(Long id) throws ExistsEventoException {
        Optional<Evento> eventoOptional = eventoRepository.findById(id);
        if(eventoOptional.isEmpty()){
            throw new ExistsEventoException("Evento with id " + id + " does not exists!");
        }
        return eventoOptional.get();
    }
}
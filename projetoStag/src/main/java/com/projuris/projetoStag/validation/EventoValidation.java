package com.projuris.projetoStag.validation;

import com.projuris.projetoStag.dtos.EventoDTO;
import com.projuris.projetoStag.entities.Evento;
import com.projuris.projetoStag.exception.ExistsEventoException;
import com.projuris.projetoStag.exception.ValidEventException;
import com.projuris.projetoStag.repositories.EventoRepository;
import lombok.experimental.UtilityClass;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class EventoValidation {

    private String message;

    public boolean validaEvento(EventoDTO eventoDTO , EventoRepository eventoRepository,Clock clock) throws ValidEventException {
        if (validDate(eventoDTO, eventoRepository) || checkDate(eventoDTO, clock) || validName(eventoDTO.getName())) {
            throw new ValidEventException(message);
        }
        return true;
    }

    public boolean validDate(EventoDTO eventoDTO, EventoRepository eventoRepository) {
        List<Evento> list = eventoRepository.findAll();
        for (Evento evento : list) {
            if (eventoDTO.getDate().isAfter(evento.getDate())
                    && eventoDTO.getDate().isBefore(evento.getDateFinal())
                    && eventoDTO.getChamber() == evento.getChamber()
                    && eventoDTO.getId() != evento.getId()) {
                message = ("Chamber used in Data!s");
                return true;
            }
            if (eventoDTO.getDateFinal().isAfter(evento.getDate())
                    && eventoDTO.getDateFinal().isBefore(evento.getDateFinal())
                    && eventoDTO.getChamber() == evento.getChamber()
                    && eventoDTO.getId() != evento.getId()) {
                message = ("Chamber used in Data!");
                return true;
            }
            if (eventoDTO.getDate().isEqual(evento.getDate())
                    && eventoDTO.getDateFinal().isEqual(evento.getDateFinal())
                    && eventoDTO.getChamber() == evento.getChamber()
                    && eventoDTO.getId() != evento.getId()) {
                message = ("Chamber used in Data!");
                return true;
            }
            if (evento.getDate().isAfter(eventoDTO.getDate())
                    && evento.getDate().isBefore(eventoDTO.getDateFinal())
                    && eventoDTO.getChamber() == evento.getChamber()
                    && eventoDTO.getId() != evento.getId()) {
                message = ("Chamber used in Data!");
                return true;
            }
            if (evento.getDateFinal().isAfter(eventoDTO.getDate())
                    && evento.getDateFinal().isBefore(eventoDTO.getDateFinal())
                    && eventoDTO.getChamber() == evento.getChamber()
                    && eventoDTO.getId() != evento.getId()) {
                message = ("Chamber used in Data!");
                return true;
            }
        }
        return false;
    }


    public boolean checkDate(EventoDTO eventoDTO, Clock clock) {
        if (eventoDTO.getDateFinal().isBefore(eventoDTO.getDate())) {
            message = ("Date invalid!");
            return true;
        }
        if (eventoDTO.getDate().isBefore(LocalDateTime.now(clock))) {
            message = ("Date invalid!");
            return true;
        }
        if (eventoDTO.getDate().isEqual(eventoDTO.getDateFinal())) {
            message = ("Date invalid!");
            return true;
        }
        if (eventoDTO.getDateFinal().getHour() - eventoDTO.getDate().getHour() >= 6) {
            message = ("Exceeded timeout!");
            return true;
        }
            if (eventoDTO.getDate().getMonth() != eventoDTO.getDateFinal().getMonth()
                    || eventoDTO.getDate().getYear() != eventoDTO.getDateFinal().getYear()
                    || eventoDTO.getDate().getDayOfMonth() != eventoDTO.getDateFinal().getDayOfMonth()) {
               message = ("Exceeded timeout!");
                return true;
            }
            return false;

    }

        public boolean validName (String name){
            if (name.length() <= 2) {
                message = ("Name invalid!");
                return true;
            }
            return false;
        }

        public Evento existsEvento(Long id, EventoRepository eventoRepository) throws ExistsEventoException {
        Optional<Evento> eventoOptional = eventoRepository.findById(id);
        if(eventoOptional.isEmpty()){
            throw new ExistsEventoException("Evento with id " + id + " does not exists!");
        }
        return eventoOptional.get();
        }
    }


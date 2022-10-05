package com.projuris.projetoStag.services;

import com.projuris.projetoStag.dtos.EventoDTO;
import com.projuris.projetoStag.entities.Evento;
import com.projuris.projetoStag.repositories.EventoRepository;
import com.projuris.projetoStag.specification.GenericSpecification;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class EventoServiceSpecification {

    private EventoServiceSpecification() {
    }

    public static GenericSpecification<EventoDTO> existsEvento(EventoRepository eventoRepository) {
        return new GenericSpecification<>() {
            @Override
            public boolean isSatisfiedBy(EventoDTO eventoDTO) {
                setMessage("Chamber used in Data!");
                List<Evento> list = eventoRepository.findAll();
                for(Evento evento : list){
                    if(eventoDTO.getDate().isAfter(evento.getDate())
                            && eventoDTO.getDate().isBefore(evento.getDateFinal())
                            && eventoDTO.getChamber().equals(evento.getChamber())
                            && eventoDTO.getId() != evento.getId()){
                        return false;
                    }
                    if(eventoDTO.getDateFinal().isAfter(evento.getDate())
                            && eventoDTO.getDateFinal().isBefore(evento.getDateFinal())
                            && eventoDTO.getChamber().equals(evento.getChamber())
                            && eventoDTO.getId() != evento.getId()){
                        return false;
                    }
                    if(eventoDTO.getDate().isEqual(evento.getDate())
                            && eventoDTO.getDateFinal().isEqual(evento.getDateFinal())
                            && eventoDTO.getChamber() == evento.getChamber()
                            && eventoDTO.getId() != evento.getId()){
                        return false;
                    }
                    if(evento.getDate().isAfter(eventoDTO.getDate())
                            && evento.getDate().isBefore(eventoDTO.getDateFinal())
                            && eventoDTO.getChamber() == evento.getChamber()
                            && eventoDTO.getId() != evento.getId()){
                        return false;
                    }
                    if(evento.getDateFinal().isAfter(eventoDTO.getDate())
                            && evento.getDateFinal().isBefore(eventoDTO.getDateFinal())
                            && eventoDTO.getChamber().equals(evento.getChamber())
                            && eventoDTO.getId() != evento.getId()){
                        return false;
                    }
                }
                return true;
            }
        };
    }

    public static GenericSpecification<EventoDTO> checkDate(Clock clock) {
        return new GenericSpecification<EventoDTO>() {
            @Override
            public boolean isSatisfiedBy(EventoDTO eventoDTO) {
                setMessage("Date invalid!");
                if(eventoDTO.getDateFinal().isBefore(eventoDTO.getDate())){
                    return false;
                }
                if(eventoDTO.getDate().isBefore(LocalDateTime.now(clock))){
                    return false;
                }
                if (eventoDTO.getDate().isEqual(eventoDTO.getDateFinal())){
                    return false;
                }
                if(eventoDTO.getDateFinal().getHour() - eventoDTO.getDate().getHour() >= 6) {
                    return false;
                }
                if(eventoDTO.getDate().getMonth() != eventoDTO.getDateFinal().getMonth()
                        || eventoDTO.getDate().getYear() != eventoDTO.getDateFinal().getYear()
                        || eventoDTO.getDate().getDayOfMonth() != eventoDTO.getDateFinal().getDayOfMonth()){
                    return false;
                }
                return true;
            }
        };
    }

    public static GenericSpecification<EventoDTO> validName() {
        return new GenericSpecification<EventoDTO>() {
            @Override
            public boolean isSatisfiedBy(EventoDTO eventoDTO) {
                setMessage("Name invalid!");
                if (eventoDTO.getName().length() <= 2) {
                    return false;
                }
                return true;
            }
        };
    }

    public static GenericSpecification<EventoDTO> checkEvento(EventoRepository eventoRepository) {
        return new GenericSpecification<EventoDTO>() {
            @Override
            public boolean isSatisfiedBy(EventoDTO eventoDTO) {
                setMessage(String.format("Evento with id %s does not exists!",eventoDTO.getId()));
                    Optional<Evento> eventoOptional = eventoRepository.findById(eventoDTO.getId());
                    if(eventoOptional.isEmpty()){
                        return false;
                    }
                    return true;
                }
            };
        }

}

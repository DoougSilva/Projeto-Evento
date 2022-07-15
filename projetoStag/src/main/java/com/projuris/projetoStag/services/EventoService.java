package com.projuris.projetoStag.services;

import com.projuris.projetoStag.dtos.EventoDTO;
import com.projuris.projetoStag.entities.Evento;
import com.projuris.projetoStag.repositories.EventoRepository;
import org.modelmapper.ModelMapper;
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

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public Object save(EventoDTO eventoDTO) {
        if(!existsEvento(eventoDTO)){
            return "Chamber used in Data";
        }
        if(!checkDate(eventoDTO.getDate(), eventoDTO.getDateFinal())){
            return "Date invalid";
        }
        var evento = new Evento();
        BeanUtils.copyProperties(eventoDTO, evento);
        eventoRepository.save(evento);
        eventoDTO.setId(evento.getId());
        return eventoDTO;
    }

    public Page<EventoDTO> findAll(PageRequest pageRequest) {
        Page<Evento> list = eventoRepository.findAll(pageRequest);
        return list.map(EventoDTO::new);
    }

    public Object updateEvento(Long id, EventoDTO eventoDTO){
        Evento evento = eventoRepository.findById(id).orElseThrow(() -> new IllegalStateException("Evento with id " + id + " does not exists."));
        eventoDTO.setId(id);
        if (eventoDTO.getName().length() > 2){
            evento.setName(eventoDTO.getName());
        } else {
            throw new IllegalStateException("Name invalid");
        }
        if(!existsEvento(eventoDTO)){
            return "Chamber used in Data";
        }
        if(!checkDate(eventoDTO.getDate(), eventoDTO.getDateFinal())){
            return "Date invalid";
        }
        evento.setDate(eventoDTO.getDate());
        evento.setDateFinal(eventoDTO.getDateFinal());
        evento.setChamber(eventoDTO.getChamber());
        eventoRepository.save(evento);
        return toEventoDTO(evento);
    }

    public Optional<EventoDTO> findByEventoId(Long id){
        if(!eventoRepository.existsById(id)){
            throw new IllegalStateException("Evento with id " + id + " does not exists.");
        }
        return eventoRepository.findById(id).map(this::toEventoDTO);
        }

    @Transactional
    public Object delete(Long id){
        if(!eventoRepository.existsById(id)){
            throw new IllegalStateException("Evento with id " + id + " does not exists.");
        }
        eventoRepository.deleteById(id);
        return "Evento deleted successfully.";
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
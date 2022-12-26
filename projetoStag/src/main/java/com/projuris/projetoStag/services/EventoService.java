package com.projuris.projetoStag.services;

import com.projuris.projetoStag.dtos.EventoDTO;
import com.projuris.projetoStag.dtos.PayloadDTO;
import com.projuris.projetoStag.entities.Evento;
import com.projuris.projetoStag.exception.ValidEventException;
import com.projuris.projetoStag.repositories.EventoRepository;
import com.projuris.projetoStag.specification.SpecificationValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.Optional;

import static com.projuris.projetoStag.services.EventoServiceSpecification.*;


@Transactional
@RequiredArgsConstructor
@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    private final ModelMapper modelMapper;

    private final Clock clock;

    public EventoDTO saveEvento(EventoDTO eventoDTO) throws ValidEventException {
        validateInsert(eventoDTO);
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

    @Transactional(readOnly = true)
    public Page<Object> findByChamber(Integer chamberCode, PageRequest pageRequest) {
        Page<Evento> list = eventoRepository.findAllByChamber(chamberCode, pageRequest);
        return list.map(EventoDTO::new);
    }

    public EventoDTO updateEvento(EventoDTO eventoDTO) throws ValidEventException {
        Evento evento = validateUpdate(eventoDTO);
        evento.setName(eventoDTO.getName());
        evento.setDate(eventoDTO.getDate());
        evento.setDateFinal(eventoDTO.getDateFinal());
        evento.setChamber(eventoDTO.getChamber());
        return toEventoDTO(evento);
    }

    @Transactional(readOnly = true)
    public Page<Object> findByEventoName(String name, PageRequest pageRequest) {
        Page<Evento> list = eventoRepository.findByNameIgnoreCase(name, pageRequest);
        return list.map(EventoDTO::new);
    }

    @Transactional(readOnly = true)
    public EventoDTO findById(Long id) {
        Optional<Evento> evento = eventoRepository.findById(id);
        if (evento.isEmpty()){
            throw new ValidEventException("Evento não existe");
        }
        return new EventoDTO(evento);
    }

    @Transactional
    public Object delete(Long id) throws ValidEventException {
        Evento evento = validateDelete(id);
        eventoRepository.delete(evento);
        return new PayloadDTO("Evento deleted successfully!");
    }

    private void validateInsert(EventoDTO eventoDTO) throws ValidEventException {
        SpecificationValidator.of()
                .add(existsEvento(eventoRepository))
                .add(checkDate(clock))
                .add(validName())
                .validateWithException(eventoDTO);
    }

    private Evento validateUpdate(EventoDTO eventoDTO) throws ValidEventException {
        SpecificationValidator.of()
                .add(checkEvento(eventoRepository))
                .add(existsEvento(eventoRepository))
                .add(checkDate(clock))
                .add(validName())
                .validateWithException(eventoDTO);
        Optional<Evento> evento = eventoRepository.findById(eventoDTO.getId());
        return evento.get();
    }

    private Evento validateDelete(Long id) throws ValidEventException {
        EventoDTO aux = EventoDTO.builder().id(id).build();
        SpecificationValidator.of()
                .add(checkEvento(eventoRepository))
                .validateWithException(aux);
        Optional<Evento> evento = eventoRepository.findById(aux.getId());
        return evento.get();
    }

    private EventoDTO toEventoDTO(Evento evento){
        return modelMapper.map(evento, EventoDTO.class);
    }
}
package com.projuris.projetoStag.resources;

import com.projuris.projetoStag.dtos.EventoDTO;
import com.projuris.projetoStag.exception.ValidEventException;
import com.projuris.projetoStag.services.EventoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path="/evento")
public class EventoResource {


    private final EventoService eventoService;

    public EventoResource(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventoDTO saveEvento(@RequestBody @Valid EventoDTO eventoDTO) throws ValidEventException {
        return eventoService.saveEvento(eventoDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Object> getAllEvento(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ){
        PageRequest pageRequeste = PageRequest.of(page, size);
        return eventoService.findAll(pageRequeste);
    }

    @GetMapping("/chamber/{chamber}")
    @ResponseStatus(HttpStatus.OK)
    public Page<Object> getByChamber(
            @PathVariable(value = "chamber") Integer chamberCode,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        PageRequest pageRequeste = PageRequest.of(page, size);
        return eventoService.findByChamber(chamberCode ,pageRequeste);
    }


    @GetMapping("/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public Page<Object> getByName(
            @PathVariable(value = "name") String name,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ){
        PageRequest pageRequeste = PageRequest.of(page, size);
        return eventoService.findByEventoName(name ,pageRequeste);
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventoDTO findById(@PathVariable(value = "id") Long id) throws ValidEventException {
        return eventoService.findById(id);
    }

    @PutMapping("/put")
    @ResponseStatus(HttpStatus.OK)
    public EventoDTO updateEvento(@RequestBody @Valid EventoDTO eventoDTO) throws ValidEventException {
        return eventoService.updateEvento(eventoDTO);
    }

    @DeleteMapping("/del-id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Object deleteEvento(@PathVariable(value = "id") Long id) throws ValidEventException {
        return eventoService.delete(id);
    }
}

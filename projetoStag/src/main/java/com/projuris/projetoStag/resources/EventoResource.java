package com.projuris.projetoStag.resources;

import com.projuris.projetoStag.dtos.EventoDTO;
import com.projuris.projetoStag.services.EventoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> saveEvento(@RequestBody @Valid EventoDTO eventoDTO){
        return eventoService.saveEvento(eventoDTO);
    }

    @GetMapping
    public ResponseEntity<Object> getAllEvento(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ){
        PageRequest pageRequeste = PageRequest.of(page, size);
        return eventoService.findAll(pageRequeste);
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<Object> getOneEvento(@PathVariable(value = "id") Long id){
            return eventoService.findByEventoId(id);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<Object> updateEvento(@PathVariable(value = "id") Long id, @RequestBody @Valid EventoDTO eventoDTO){
        return eventoService.updateEvento(id, eventoDTO);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Object> deleteEvento(@PathVariable(value = "id") Long id){
        return eventoService.delete(id);
    }
}

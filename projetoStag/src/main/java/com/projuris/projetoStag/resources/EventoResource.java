package com.projuris.projetoStag.resources;

import com.projuris.projetoStag.dtos.EventoDTO;
import com.projuris.projetoStag.exception.ExistsEventoException;
import com.projuris.projetoStag.exception.ValidEventException;
import com.projuris.projetoStag.services.EventoService;
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
    public ResponseEntity<Object> saveEvento(@RequestBody @Valid EventoDTO eventoDTO) throws ValidEventException, ExistsEventoException {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoService.saveEvento(eventoDTO));
    }

    @GetMapping
    public ResponseEntity<Object> getAllEvento(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ){
        PageRequest pageRequeste = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.findAll(pageRequeste));
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<Object> getOneEvento(@PathVariable(value = "id") Long id) throws ExistsEventoException {
            return ResponseEntity.status(HttpStatus.OK).body(eventoService.findByEventoId(id));
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<Object> updateEvento(@PathVariable(value = "id") Long id, @RequestBody @Valid EventoDTO eventoDTO) throws ValidEventException, ExistsEventoException {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(eventoService.updateEvento(id, eventoDTO));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Object> deleteEvento(@PathVariable(value = "id") Long id) throws ExistsEventoException {
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.delete(id));
    }
}

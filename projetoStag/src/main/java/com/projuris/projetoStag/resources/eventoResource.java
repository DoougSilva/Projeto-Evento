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
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/evento")
public class eventoResource {


    private final EventoService eventoService;

    public eventoResource(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @PostMapping
    public ResponseEntity<Object> saveEvento(@RequestBody @Valid EventoDTO eventoDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoService.save(eventoDTO));
    }

    @GetMapping
    public ResponseEntity<Page<EventoDTO>> getAllEvento(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ){
        PageRequest pageRequeste = PageRequest.of(page, size);
        Page<EventoDTO> list = eventoService.findAll(pageRequeste);
        return ResponseEntity.ok(list);
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<Object> getOneEvento(@PathVariable(value = "id") Long id){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(eventoService.findByEventoId(id));
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<Object> updateEvento(@PathVariable(value = "id") Long id, @RequestBody @Valid EventoDTO eventoDTO){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.updateEvento(id, eventoDTO));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Object> deleteEvento(@PathVariable(value = "id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.delete(id));
    }
}

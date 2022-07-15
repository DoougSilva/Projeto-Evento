package com.projuris.projetoStag.resources;

import com.projuris.projetoStag.dtos.EventoDTO;
import com.projuris.projetoStag.services.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/evento")
public class eventoResource {

    @Autowired
    private EventoService eventoService;

    @PostMapping
    public ResponseEntity<Object> saveEvento(@RequestBody @Valid EventoDTO eventoDTO){
        if(!eventoService.existsEvento(eventoDTO.getDate(), eventoDTO.getDateFinal(), eventoDTO.getChamber())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Chamber used in Data");
        }
        if(!eventoService.checkDate(eventoDTO.getDate(), eventoDTO.getDateFinal())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Date invalid");
        }
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


    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneEvento(@PathVariable(value = "id") Long id){
        Optional<EventoDTO> eventoOptional = eventoService.findById(id);
        if(!eventoOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(eventoOptional.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateEvento(@PathVariable(value = "id") Long id, @RequestBody @Valid EventoDTO eventoDTO){
        Optional<EventoDTO> eventoOptional = eventoService.findById(id);
        if(!eventoOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento not found.");
        }
        if(!eventoService.existsEvento(eventoDTO.getDate(), eventoDTO.getDateFinal(), eventoDTO.getChamber())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Chamber used in Data");
        }
        if(!eventoService.checkDate(eventoDTO.getDate(), eventoDTO.getDateFinal())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Date invalid");
        }
        eventoDTO.setId(eventoOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.save(eventoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEvento(@PathVariable(value = "id") Long id){
        Optional<EventoDTO> eventoOptional = eventoService.findById(id);
        if(!eventoOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento not found.");
        }
        eventoService.delete(eventoOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Evento deleted successfully.");
    }
}

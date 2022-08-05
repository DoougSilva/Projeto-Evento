package com.projuris.projetoStag.evento;

import com.projuris.projetoStag.dtos.EventoDTO;
import com.projuris.projetoStag.entities.Evento;
import com.projuris.projetoStag.exception.ExistsEventoException;
import com.projuris.projetoStag.exception.ValidEventException;
import com.projuris.projetoStag.helper.EventoMockHelper;
import com.projuris.projetoStag.repositories.EventoRepository;
import com.projuris.projetoStag.services.EventoService;
import com.projuris.projetoStag.validation.EventoValidation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class EventoServiceUnitTest{

    EventoService eventoService;

    @Mock
    private static EventoRepository eventoRepository;

    @Mock
    private static ModelMapper modelMapper;

    @Mock
    private static Clock clock;

    private static ZonedDateTime NOW = ZonedDateTime.of(
            2022,
            7,
            28,
            8,
            00,
            00,
            00,
            ZoneId.of("GMT")

    );

    private EventoDTO eventoDTO;

    private Evento evento;


    Optional<Evento> eventoOptional;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        eventoService = new EventoService(eventoRepository, modelMapper, clock);
        when(clock.getZone()).thenReturn(NOW.getZone());
        when(clock.instant()).thenReturn(NOW.toInstant());

        this.eventoDTO = EventoMockHelper.createEventoDTO();
        this.evento = EventoMockHelper.createEvento();

    }

    @Test
    public void devePersistitUmEvento() throws Exception {
        eventoService.saveEvento(eventoDTO);
        verify(eventoRepository, Mockito.times(1)).save(ArgumentMatchers.any(Evento.class));
    }
    @Test
    public void deveAtualizarUmEvento() throws Exception{
        eventoDTO.setName("TesteDoTest");
        eventoDTO.setDate(LocalDateTime.of(2022,7,28, 8, 30));
        eventoDTO.setDateFinal(LocalDateTime.of(2022,7,28, 9, 00));
        evento.setDate(LocalDateTime.of(2022,7,28, 8, 00));
        evento.setDateFinal(LocalDateTime.of(2022,7,28, 10,00 ));
        eventoOptional = Optional.of(evento);
        when(eventoRepository.findById(1L)).thenReturn(eventoOptional);
        eventoService.updateEvento(evento.getId(), eventoDTO);
        Assertions.assertEquals(eventoOptional.get().getName(), eventoDTO.getName());
        Assertions.assertEquals(eventoOptional.get().getDate(), eventoDTO.getDate());
        Assertions.assertEquals(eventoOptional.get().getDateFinal(), eventoDTO.getDateFinal());
        Assertions.assertEquals(eventoOptional.get().getChamber(), eventoDTO.getChamber());
    }

    @Test
    public void deveRetornarErroAoAtualizarUmEventoQueNaoExiste(){
        Assertions.assertThrows(ExistsEventoException.class, () -> {
            eventoService.updateEvento(evento.getId(), eventoDTO);
        } );
    }
    @Test
    public void deveRetornarUmaListaDeEventos() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Evento> list = Collections.singletonList(evento);
        Page<Evento> lista = new PageImpl<Evento>(list);
        when(eventoRepository.findAll(ArgumentMatchers.eq(pageRequest))).thenReturn(lista);
        eventoService.findAll(pageRequest);
        verify(eventoRepository, Mockito.times(1)).findAll(ArgumentMatchers.any(PageRequest.class));
    }

   @Test
    public void deveRetornarErroAoBuscarUmEventoQueNaoExiste()throws Exception {
        Assertions.assertThrows(ExistsEventoException.class, () -> {
            eventoService.findByEventoId(1L);
        } );
    }


    @Test
    public void deveRetornarUmEvento() throws Exception {
        eventoOptional = Optional.of(evento);
        when(eventoRepository.findById(evento.getId())).thenReturn(eventoOptional);
        eventoService.findByEventoId(evento.getId());
        verify(eventoRepository, Mockito.times(1)).findById(ArgumentMatchers.any(Long.class));
    }

    @Test
    public void deveRemoverUmEvento() throws Exception {
        eventoOptional = Optional.of(evento);
        when(eventoRepository.findById(evento.getId())).thenReturn(eventoOptional);
        eventoService.delete(evento.getId());
        verify(eventoRepository, Mockito.times(1)).delete(evento);
    }

    @Test
    public void deveRetornarErroAoRemoverUmEventoQueNaoExiste() throws Exception {
        Assertions.assertThrows(ExistsEventoException.class, () -> {
            eventoService.delete(evento.getId());
        });
    }
}

package com.projuris.projetoStag;

import com.projuris.projetoStag.dtos.EventoDTO;
import com.projuris.projetoStag.entities.Chamber;
import com.projuris.projetoStag.entities.Evento;
import com.projuris.projetoStag.repositories.EventoRepository;
import com.projuris.projetoStag.services.EventoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
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

    private List<Evento> list;

    Optional<Evento> eventoOptional;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        eventoService = new EventoService(eventoRepository, modelMapper, clock);
        when(clock.getZone()).thenReturn(NOW.getZone());
        when(clock.instant()).thenReturn(NOW.toInstant());
        eventoDTO = EventoDTO.builder()
                .name("Test")
                .chamber(Chamber.SALA_1)
                .build();
        evento = Evento.builder()
                .id(1L)
                .name("Test")
                .chamber(Chamber.SALA_1)
                .build();
        list = Collections.singletonList(evento);
        eventoOptional = Optional.of(evento);
        when(eventoRepository.findAll()).thenReturn(list);
        when(eventoRepository.save(ArgumentMatchers.any(Evento.class))).thenReturn(evento);
    }

    @Test // todas as validações corretas
    public void devePersistitUmEvento() {
        eventoDTO.setDate(LocalDateTime.of(2022, 7, 28, 9, 00));
        eventoDTO.setDateFinal(LocalDateTime.of(2022, 7, 28, 10, 00));
        evento.setDate(LocalDateTime.of(2022, 7, 28, 8, 00));
        evento.setDateFinal(LocalDateTime.of(2022, 7, 28, 8, 30));
        when(eventoRepository.findById(ArgumentMatchers.eq(eventoDTO.getId()))).thenReturn(eventoOptional);
        eventoService.saveEvento(eventoDTO);
        verify(eventoRepository, Mockito.times(1)).save(ArgumentMatchers.any(Evento.class));
    }

    @Test // teste da validação existsEvento cenario 01
    public void deveRetornarErroAoPersistitUmEventoComDataInicialEtiverDentroDeOutroEvento(){
        eventoDTO.setDate(LocalDateTime.of(2022,7,28, 8, 15));
        eventoDTO.setDateFinal(LocalDateTime.of(2022,7,28, 8, 45));
        evento.setDate(LocalDateTime.of(2022,7,28, 8, 00));
        evento.setDateFinal(LocalDateTime.of(2022,7,28, 8, 30));
        eventoService.saveEvento(eventoDTO);
        Mockito.verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

    @Test // teste da validação existsEvento cenario 02
    public void deveRetornarErroAoPersistitUmEventoComDataFinalEtiverDentroDeOutroEvento(){
        eventoDTO.setDate(LocalDateTime.of(2022,7,28, 8, 00));
        eventoDTO.setDateFinal(LocalDateTime.of(2022,7,28, 8, 45));
        evento.setDate(LocalDateTime.of(2022,7,28, 8, 30));
        evento.setDateFinal(LocalDateTime.of(2022,7,28, 9, 00));
        eventoService.saveEvento(eventoDTO);
        verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

    @Test // teste da validação existsEvento cenario 03
    public void deveRetornarErroAoPersistitUmEventoComDatasDeUmEventoExistente(){
        eventoDTO.setDate(LocalDateTime.of(2022,7,28, 8, 00));
        eventoDTO.setDateFinal(LocalDateTime.of(2022,7,28, 8, 45));
        evento.setDate(LocalDateTime.of(2022,7,28, 8, 00));
        evento.setDateFinal(LocalDateTime.of(2022,7,28, 8, 45));
        eventoService.saveEvento(eventoDTO);
        verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
    }
    @Test // teste da validação existsEvento cenario 04
    public void deveRetornarErroAoPersistitUmEventoComDatasEntreUmEventoExistente(){
        eventoDTO.setDate(LocalDateTime.of(2022,7,28, 8, 00));
        eventoDTO.setDateFinal(LocalDateTime.of(2022,7,28, 9, 00));
        evento.setDate(LocalDateTime.of(2022,7,28, 8, 10));
        evento.setDateFinal(LocalDateTime.of(2022,7,28, 8,50 ));
        eventoService.saveEvento(eventoDTO);
        verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

    @Test // teste da validação  existsEvento cenario 05
    public void deveRetornarErroAoPersistitUmEventoComNomeInvalido(){
        eventoDTO.setName("");
        eventoDTO.setDate(LocalDateTime.of(2022,7,28, 9, 00));
        eventoDTO.setDateFinal(LocalDateTime.of(2022,7,28, 9, 30));
        evento.setDate(LocalDateTime.of(2022,7,28, 10, 00));
        evento.setDateFinal(LocalDateTime.of(2022,7,28, 11,00 ));
        eventoService.saveEvento(eventoDTO);
        verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

    @Test // teste da validação checkDate cenario 01
    public void deveRetornarErroAoPersistitUmEventoComDataFinalAnteriorDaDataInicial(){
        eventoDTO.setDate(LocalDateTime.of(2022,7,28, 9, 00));
        eventoDTO.setDateFinal(LocalDateTime.of(2022,7,28, 8, 00));
        evento.setDate(LocalDateTime.of(2022,7,28, 9, 00));
        evento.setDateFinal(LocalDateTime.of(2022,7,28, 10,00 ));
        eventoService.saveEvento(eventoDTO);
        verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

    @Test // teste da validação checkDate cenario 02
    public void deveRetornarErroAoPersistitUmEventoComDataInicialAnteriorDaDataAtual(){
        eventoDTO.setDate(LocalDateTime.of(2022,7,28, 7, 59));
        eventoDTO.setDateFinal(LocalDateTime.of(2022,7,28, 9, 00));
        evento.setDate(LocalDateTime.of(2022,7,28, 9, 00));
        evento.setDateFinal(LocalDateTime.of(2022,7,28, 10,00 ));
        eventoService.saveEvento(eventoDTO);
        verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

    @Test // teste da validação checkDate cenario 03
    public void deveRetornarErroAoPersistitUmEventoComDataInicialIgualDataFinal(){
        eventoDTO.setDate(LocalDateTime.of(2022,7,28, 9, 00));
        eventoDTO.setDateFinal(LocalDateTime.of(2022,7,28, 9, 00));
        evento.setDate(LocalDateTime.of(2022,7,28, 10, 00));
        evento.setDateFinal(LocalDateTime.of(2022,7,28, 11,00 ));
        eventoService.saveEvento(eventoDTO);
        verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

    @Test
    public void deveAtualizarUmEvento(){
        Long eventoId = 1L;
        evento.setName("TesteDoTest");
        eventoDTO.setDate(LocalDateTime.of(2022,7,28, 8, 30));
        eventoDTO.setDateFinal(LocalDateTime.of(2022,7,28, 9, 00));
        evento.setDate(LocalDateTime.of(2022,7,28, 8, 00));
        evento.setDateFinal(LocalDateTime.of(2022,7,28, 10,00 ));
        when(eventoRepository.findById(ArgumentMatchers.eq(evento.getId()))).thenReturn(eventoOptional);
        eventoService.updateEvento(eventoId, eventoDTO);
        Assertions.assertEquals(eventoOptional.get().getName(), eventoDTO.getName());
        Assertions.assertEquals(eventoOptional.get().getDate(), eventoDTO.getDate());
        Assertions.assertEquals(eventoOptional.get().getDateFinal(), eventoDTO.getDateFinal());
        Assertions.assertEquals(eventoOptional.get().getChamber(), eventoDTO.getChamber());
    }
    @Test
    public void deveRetornarUmaListaDeEventos(){
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Evento> list = Mockito.mock(Page.class);
        when(eventoRepository.findAll(ArgumentMatchers.eq(pageRequest))).thenReturn(list);
        eventoService.findAll(pageRequest);
        verify(eventoRepository, Mockito.times(1)).findAll(ArgumentMatchers.any(PageRequest.class));
    }

    @Test
    public void deveRetornarErroAoBuscarUmEventoQueNaoExiste(){
        Long eventoId = 1L;
        when(eventoRepository.existsById(ArgumentMatchers.eq(eventoId))).thenReturn(Boolean.FALSE);
        eventoService.findByEventoId(eventoId);
        verify(eventoRepository, Mockito.never()).findBy(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void deveRetornarUmEvento(){
        Long eventoId = 1L;
        when(eventoRepository.existsById(ArgumentMatchers.eq(eventoId))).thenReturn(Boolean.TRUE);
        Evento evento = Mockito.mock(Evento.class);
        Optional<Evento> eventoOptional = Optional.of(evento);
        when(eventoRepository.findById(ArgumentMatchers.eq(eventoId))).thenReturn(eventoOptional);
        eventoService.findByEventoId(eventoId);
        verify(eventoRepository, Mockito.times(1)).findById(ArgumentMatchers.any(Long.class));
    }
    @Test
    public void deveRemoverUmEvento(){
        Long eventoId = 1L;
        when(eventoRepository.existsById(ArgumentMatchers.eq(eventoId))).thenReturn(Boolean.TRUE);
        eventoService.delete(eventoId);
        verify(eventoRepository, Mockito.times(1)).deleteById(ArgumentMatchers.any(Long.class));
    }

    @Test
    public void deveRetornarErroAoRemoverUmEventoQueNaoExiste(){
        Long eventoId = 1L;
        when(eventoRepository.existsById(ArgumentMatchers.eq(eventoId))).thenReturn(Boolean.FALSE);
        eventoService.delete(eventoId);
        verify(eventoRepository, Mockito.never()).deleteById(ArgumentMatchers.any());
    }
}

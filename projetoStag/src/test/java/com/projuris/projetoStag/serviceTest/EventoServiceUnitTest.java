package com.projuris.projetoStag.serviceTest;

import com.projuris.projetoStag.config.ApplicationConfigTest;
import com.projuris.projetoStag.dtos.EventoDTO;
import com.projuris.projetoStag.entities.Chamber;
import com.projuris.projetoStag.entities.Evento;
import com.projuris.projetoStag.repositories.EventoRepository;
import com.projuris.projetoStag.services.EventoService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
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


public class EventoServiceUnitTest extends ApplicationConfigTest {

    @Autowired
    EventoService eventoService;

    @MockBean
    private static EventoRepository eventoRepository;

    @MockBean
    private static ModelMapper modelMapper;

    @MockBean
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

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        when(clock.getZone()).thenReturn(NOW.getZone());
        when(clock.instant()).thenReturn(NOW.toInstant());
    }
    @Test // todas as validações corretas
    public void devePersistitUmEvento(){
        EventoDTO eventoDTO = EventoDTO.builder()
                .name("Test")
                .date(LocalDateTime.of(2022,7,28, 9, 00))
                .dateFinal(LocalDateTime.of(2022,7,28, 10, 00))
                .chamber(Chamber.SALA_1)
                .build();
        Evento evento = Evento.builder()
                .name("Test")
                .date(LocalDateTime.of(2022,7,28, 8, 00))
                .dateFinal(LocalDateTime.of(2022,7,28, 8, 30))
                .chamber(Chamber.SALA_1)
                .build();
        List<Evento> list = Collections.singletonList(evento);
        when(eventoRepository.findAll()).thenReturn(list);
        Optional<Evento> eventoOptional = Optional.of(evento);
        when(eventoRepository.findById(ArgumentMatchers.eq(eventoDTO.getId()))).thenReturn(eventoOptional);
        when(eventoRepository.save(ArgumentMatchers.any(Evento.class))).thenReturn(evento);
        eventoService.saveEvento(eventoDTO);
        verify(eventoRepository, Mockito.times(1)).save(ArgumentMatchers.any(Evento.class));
    }

    @Test // teste da validação existsEvento cenario 01
    public void deveRetornarErroAoPersistitUmEventoComDataInicialEtiverDentroDeOutroEvento(){
        EventoDTO eventoDTO = EventoDTO.builder()
                .name("Test")
                .date(LocalDateTime.of(2022,7,28, 8, 15))
                .dateFinal(LocalDateTime.of(2022,7,28, 8, 45))
                .chamber(Chamber.SALA_1)
                .build();
        Evento evento = Evento.builder()
                .name("Test")
                .date(LocalDateTime.of(2022,7,28, 8, 00))
                .dateFinal(LocalDateTime.of(2022,7,28, 8, 30))
                .chamber(Chamber.SALA_1)
                .build();
        List<Evento> list = Collections.singletonList(evento);
        when(eventoRepository.findAll()).thenReturn(list);
        Optional<Evento> eventoOptional = Optional.of(evento);
        when(eventoRepository.findById(ArgumentMatchers.eq(eventoDTO.getId()))).thenReturn(eventoOptional);
        when(eventoRepository.save(ArgumentMatchers.any(Evento.class))).thenReturn(evento);
        eventoService.saveEvento(eventoDTO);
        Mockito.verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

    @Test // teste da validação existsEvento cenario 02
    public void deveRetornarErroAoPersistitUmEventoComDataFinalEtiverDentroDeOutroEvento(){
        EventoDTO eventoDTO = EventoDTO.builder()
                .name("Test")
                .date(LocalDateTime.of(2022,7,28, 8, 00))
                .dateFinal(LocalDateTime.of(2022,7,28, 8, 45))
                .chamber(Chamber.SALA_1)
                .build();
        Evento evento = Evento.builder()
                .name("Test")
                .date(LocalDateTime.of(2022,7,28, 8, 30))
                .dateFinal(LocalDateTime.of(2022,7,28, 9, 00))
                .chamber(Chamber.SALA_1)
                .build();
        List<Evento> list = Collections.singletonList(evento);
        when(eventoRepository.findAll()).thenReturn(list);
        Optional<Evento> eventoOptional = Optional.of(evento);
        when(eventoRepository.findById(ArgumentMatchers.eq(eventoDTO.getId()))).thenReturn(eventoOptional);
        when(eventoRepository.save(ArgumentMatchers.any(Evento.class))).thenReturn(evento);
        eventoService.saveEvento(eventoDTO);
        verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

    @Test // teste da validação existsEvento cenario 03
    public void deveRetornarErroAoPersistitUmEventoComDatasDeUmEventoExistente(){
        EventoDTO eventoDTO = EventoDTO.builder()
                .name("Test")
                .date(LocalDateTime.of(2022,7,28, 8, 00))
                .dateFinal(LocalDateTime.of(2022,7,28, 8, 45))
                .chamber(Chamber.SALA_1)
                .build();
        Evento evento = Evento.builder()
                .name("Test")
                .date(LocalDateTime.of(2022,7,28, 8, 00))
                .dateFinal(LocalDateTime.of(2022,7,28, 8, 45))
                .chamber(Chamber.SALA_1)
                .build();
        List<Evento> list = Collections.singletonList(evento);
        when(eventoRepository.findAll()).thenReturn(list);
        Optional<Evento> eventoOptional = Optional.of(evento);
        when(eventoRepository.findById(ArgumentMatchers.eq(eventoDTO.getId()))).thenReturn(eventoOptional);
        when(eventoRepository.save(ArgumentMatchers.any(Evento.class))).thenReturn(evento);
        eventoService.saveEvento(eventoDTO);
        verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
    }
    @Test // teste da validação existsEvento cenario 04
    public void deveRetornarErroAoPersistitUmEventoComDatasEntreUmEventoExistente(){
        EventoDTO eventoDTO = EventoDTO.builder()
                .name("Test")
                .date(LocalDateTime.of(2022,7,28, 8, 00))
                .dateFinal(LocalDateTime.of(2022,7,28, 9, 00))
                .chamber(Chamber.SALA_1)
                .build();
        Evento evento = Evento.builder()
                .name("Test")
                .date(LocalDateTime.of(2022,7,28, 8, 10))
                .dateFinal(LocalDateTime.of(2022,7,28, 8,50 ))
                .chamber(Chamber.SALA_1)
                .build();
        List<Evento> list = Collections.singletonList(evento);
        when(eventoRepository.findAll()).thenReturn(list);
        Optional<Evento> eventoOptional = Optional.of(evento);
        when(eventoRepository.findById(ArgumentMatchers.eq(eventoDTO.getId()))).thenReturn(eventoOptional);
        when(eventoRepository.save(ArgumentMatchers.any(Evento.class))).thenReturn(evento);
        eventoService.saveEvento(eventoDTO);
        verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

    @Test // teste da validação checkDate cenario 01
    public void deveRetornarErroAoPersistitUmEventoComDataFinalAnteriorDaDataInicial(){
        EventoDTO eventoDTO = EventoDTO.builder()
                .name("Test")
                .date(LocalDateTime.of(2022,7,28, 9, 00))
                .dateFinal(LocalDateTime.of(2022,7,28, 8, 00))
                .chamber(Chamber.SALA_1)
                .build();
        Evento evento = Evento.builder()
                .name("Test")
                .date(LocalDateTime.of(2022,7,28, 9, 00))
                .dateFinal(LocalDateTime.of(2022,7,28, 10,00 ))
                .chamber(Chamber.SALA_1)
                .build();
        List<Evento> list = Collections.singletonList(evento);
        when(eventoRepository.findAll()).thenReturn(list);
        Optional<Evento> eventoOptional = Optional.of(evento);
        when(eventoRepository.findById(ArgumentMatchers.eq(eventoDTO.getId()))).thenReturn(eventoOptional);
        when(eventoRepository.save(ArgumentMatchers.any(Evento.class))).thenReturn(evento);
        eventoService.saveEvento(eventoDTO);
        verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

    @Test // teste da validação checkDate cenario 02
    public void deveRetornarErroAoPersistitUmEventoComDataInicialAnteriorDaDataAtual(){
        EventoDTO eventoDTO = EventoDTO.builder()
                .name("Test")
                .date(LocalDateTime.of(2022,7,28, 7, 59))
                .dateFinal(LocalDateTime.of(2022,7,28, 9, 00))
                .chamber(Chamber.SALA_1)
                .build();
        Evento evento = Evento.builder()
                .name("Test")
                .date(LocalDateTime.of(2022,7,28, 9, 00))
                .dateFinal(LocalDateTime.of(2022,7,28, 10,00 ))
                .chamber(Chamber.SALA_1)
                .build();
        List<Evento> list = Collections.singletonList(evento);
        when(eventoRepository.findAll()).thenReturn(list);
        Optional<Evento> eventoOptional = Optional.of(evento);
        when(eventoRepository.findById(ArgumentMatchers.eq(eventoDTO.getId()))).thenReturn(eventoOptional);
        when(eventoRepository.save(ArgumentMatchers.any(Evento.class))).thenReturn(evento);
        eventoService.saveEvento(eventoDTO);
        verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

    @Test // teste da validação checkDate cenario 03
    public void deveRetornarErroAoPersistitUmEventoComDataInicialIgualDataFinal(){
        EventoDTO eventoDTO = EventoDTO.builder()
                .name("Test")
                .date(LocalDateTime.of(2022,7,28, 9, 00))
                .dateFinal(LocalDateTime.of(2022,7,28, 9, 00))
                .chamber(Chamber.SALA_1)
                .build();
        Evento evento = Evento.builder()
                .name("Test")
                .date(LocalDateTime.of(2022,7,28, 10, 00))
                .dateFinal(LocalDateTime.of(2022,7,28, 11,00 ))
                .chamber(Chamber.SALA_1)
                .build();
        List<Evento> list = Collections.singletonList(evento);
        when(eventoRepository.findAll()).thenReturn(list);
        Optional<Evento> eventoOptional = Optional.of(evento);
        when(eventoRepository.findById(ArgumentMatchers.eq(eventoDTO.getId()))).thenReturn(eventoOptional);
        when(eventoRepository.save(ArgumentMatchers.any(Evento.class))).thenReturn(evento);
        eventoService.saveEvento(eventoDTO);
        verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

    @Test
    public void deveAtualizarUmEvento(){
        Long eventoId = 1L;
        EventoDTO eventoDTO = EventoDTO.builder()
                .name("Test")
                .date(LocalDateTime.of(2022,7,28, 9, 30))
                .dateFinal(LocalDateTime.of(2022,7,28, 10, 00))
                .chamber(Chamber.SALA_1)
                .build();
        Evento evento = Evento.builder()
                .id(1L)
                .name("Test")
                .date(LocalDateTime.of(2022,7,28, 9, 00))
                .dateFinal(LocalDateTime.of(2022,7,28, 10,00 ))
                .chamber(Chamber.SALA_1)
                .build();
        List<Evento> list = Collections.singletonList(evento);
        when(eventoRepository.findAll()).thenReturn(list);
        Optional<Evento> eventoOptional = Optional.of(evento);
        when(eventoRepository.findById(ArgumentMatchers.eq(eventoDTO.getId()))).thenReturn(eventoOptional);
        eventoService.updateEvento(eventoId, eventoDTO);

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

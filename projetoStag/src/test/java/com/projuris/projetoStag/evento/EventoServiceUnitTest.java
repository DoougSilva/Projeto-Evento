package com.projuris.projetoStag.evento;

import com.projuris.projetoStag.dtos.EventoDTO;
import com.projuris.projetoStag.entities.Evento;
import com.projuris.projetoStag.exception.ExistsEventoException;
import com.projuris.projetoStag.exception.ValidEventException;
import com.projuris.projetoStag.helper.EventoMockHelper;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class EventoServiceUnitTest {

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

    private Optional<Evento> eventoOptional;

    @BeforeEach
    void setUp() {
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
    public void deveAtualizarUmEvento() throws Exception {
        eventoDTO.setName("TesteDoTest");
        eventoDTO.setDate(LocalDateTime.of(2022, 7, 28, 8, 30));
        eventoDTO.setDateFinal(LocalDateTime.of(2022, 7, 28, 9, 00));
        evento.setDate(LocalDateTime.of(2022, 7, 28, 8, 00));
        evento.setDateFinal(LocalDateTime.of(2022, 7, 28, 10, 00));
        eventoOptional = Optional.of(evento);
        when(eventoRepository.findById(1L)).thenReturn(eventoOptional);
        eventoService.updateEvento(evento.getId(), eventoDTO);
        Assertions.assertEquals(eventoOptional.get().getName(), eventoDTO.getName());
        Assertions.assertEquals(eventoOptional.get().getDate(), eventoDTO.getDate());
        Assertions.assertEquals(eventoOptional.get().getDateFinal(), eventoDTO.getDateFinal());
        Assertions.assertEquals(eventoOptional.get().getChamber(), eventoDTO.getChamber());
    }

    @Test
    public void deveRetornarUmaListaDeEventos() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Evento> list = Collections.singletonList(evento);
        Page<Evento> lista = new PageImpl<Evento>(list);
        when(eventoRepository.findAll(ArgumentMatchers.eq(pageRequest))).thenReturn(lista);
        eventoService.findAll(pageRequest);
        verify(eventoRepository, Mockito.times(1)).findAll(ArgumentMatchers.any(PageRequest.class));
    }

    @Test
    public void deveRetornarUmEvento() throws Exception {
        eventoOptional = Optional.of(evento);
        when(eventoRepository.findByNameIgnoreCase(evento.getName())).thenReturn(eventoOptional);
        eventoService.findByEventoName(evento.getName());verify(eventoRepository, Mockito.times(1)).findByNameIgnoreCase(ArgumentMatchers.any(String.class));
    }

    @Test
    public void deveRemoverUmEvento() throws Exception {
        eventoOptional = Optional.of(evento);
        when(eventoRepository.findById(evento.getId())).thenReturn(eventoOptional);
        eventoService.delete(evento.getId());
        verify(eventoRepository, Mockito.times(1)).delete(evento);
    }

    @Test // teste da validação existsEvento cenario 01
    public void deveRetornarErroAoPersistitUmEventoComDataInicialEtiverDentroDeOutroEvento() {
        eventoDTO.setDate(LocalDateTime.of(2022, 7, 28, 8, 15));
        eventoDTO.setDateFinal(LocalDateTime.of(2022, 7, 28, 8, 45));
        evento.setDate(LocalDateTime.of(2022, 7, 28, 8, 00));
        evento.setDateFinal(LocalDateTime.of(2022, 7, 28, 8, 30));
        List<Evento> list;
        list = Collections.singletonList(evento);
        when(eventoRepository.findAll()).thenReturn(list);
        assertThrows(ValidEventException.class, () -> {
            eventoService.saveEvento(eventoDTO);
        });
    }

    @Test // teste da validação existsEvento cenario 02
    public void deveRetornarErroAoPersistitUmEventoComDataFinalEtiverDentroDeOutroEvento() {
        eventoDTO.setDate(LocalDateTime.of(2022, 7, 28, 8, 00));
        eventoDTO.setDateFinal(LocalDateTime.of(2022, 7, 28, 8, 45));
        evento.setDate(LocalDateTime.of(2022, 7, 28, 8, 30));
        evento.setDateFinal(LocalDateTime.of(2022, 7, 28, 9, 00));
        List<Evento> list;
        list = Collections.singletonList(evento);
        when(eventoRepository.findAll()).thenReturn(list);
        assertThrows(ValidEventException.class, () -> {
            eventoService.saveEvento(eventoDTO);
        });
    }

    @Test // teste da validação existsEvento cenario 03
    public void deveRetornarErroAoPersistitUmEventoComDatasDeUmEventoExistente() {
        eventoDTO.setDate(LocalDateTime.of(2022, 7, 28, 8, 00));
        eventoDTO.setDateFinal(LocalDateTime.of(2022, 7, 28, 8, 45));
        evento.setDate(LocalDateTime.of(2022, 7, 28, 8, 00));
        evento.setDateFinal(LocalDateTime.of(2022, 7, 28, 8, 45));
        List<Evento> list;
        list = Collections.singletonList(evento);
        when(eventoRepository.findAll()).thenReturn(list);
        assertThrows(ValidEventException.class, () -> {
            eventoService.saveEvento(eventoDTO);
        });
    }

    @Test // teste da validação existsEvento cenario 04
    public void deveRetornarErroAoPersistitUmEventoComDatasEntreUmEventoExistente() {
        eventoDTO.setDate(LocalDateTime.of(2022, 7, 28, 8, 00));
        eventoDTO.setDateFinal(LocalDateTime.of(2022, 7, 28, 9, 00));
        evento.setDate(LocalDateTime.of(2022, 7, 28, 8, 10));
        evento.setDateFinal(LocalDateTime.of(2022, 7, 28, 8, 50));
        List<Evento> list;
        list = Collections.singletonList(evento);
        when(eventoRepository.findAll()).thenReturn(list);
        assertThrows(ValidEventException.class, () -> {
            eventoService.saveEvento(eventoDTO);
        });
    }

    @Test // teste da validação  existsEvento cenario 05
    public void deveRetornarErroAoPersistitUmEventoComNomeInvalido() {
        eventoDTO.setName("");
        eventoDTO.setDate(LocalDateTime.of(2022, 7, 28, 9, 00));
        eventoDTO.setDateFinal(LocalDateTime.of(2022, 7, 28, 9, 30));
        evento.setDate(LocalDateTime.of(2022, 7, 28, 10, 00));
        evento.setDateFinal(LocalDateTime.of(2022, 7, 28, 11, 00));
        List<Evento> list;
        list = Collections.singletonList(evento);
        when(eventoRepository.findAll()).thenReturn(list);
        assertThrows(ValidEventException.class, () -> {
            eventoService.saveEvento(eventoDTO);
        });
    }

    @Test // teste da validação checkDate cenario 01
    public void deveRetornarErroAoPersistitUmEventoComDataFinalAnteriorDaDataInicial() {
        eventoDTO.setDate(LocalDateTime.of(2022, 7, 28, 9, 00));
        eventoDTO.setDateFinal(LocalDateTime.of(2022, 7, 28, 8, 00));
        evento.setDate(LocalDateTime.of(2022, 7, 28, 9, 00));
        evento.setDateFinal(LocalDateTime.of(2022, 7, 28, 10, 00));
        assertThrows(ValidEventException.class, () -> {
            eventoService.saveEvento(eventoDTO);
        });
    }

    @Test // teste da validação checkDate cenario 02
    public void deveRetornarErroAoPersistitUmEventoComDataInicialAnteriorDaDataAtual() {
        eventoDTO.setDate(LocalDateTime.of(2022, 7, 28, 7, 59));
        eventoDTO.setDateFinal(LocalDateTime.of(2022, 7, 28, 9, 00));
        evento.setDate(LocalDateTime.of(2022, 7, 28, 9, 00));
        evento.setDateFinal(LocalDateTime.of(2022, 7, 28, 10, 00));
        assertThrows(ValidEventException.class, () -> {
            eventoService.saveEvento(eventoDTO);
        });
    }

    @Test // teste da validação checkDate cenario 03
    public void deveRetornarErroAoPersistitUmEventoComDataInicialIgualDataFinal() {
        eventoDTO.setDate(LocalDateTime.of(2022, 7, 28, 9, 00));
        eventoDTO.setDateFinal(LocalDateTime.of(2022, 7, 28, 9, 00));
        evento.setDate(LocalDateTime.of(2022, 7, 28, 10, 00));
        evento.setDateFinal(LocalDateTime.of(2022, 7, 28, 11, 00));
        assertThrows(ValidEventException.class, () -> {
            eventoService.saveEvento(eventoDTO);
        });
    }

    @Test // teste da validação checkDate cenario 04
    public void deveRetornarErroAoPersistitUmEventoComDataExedendoTempoLimite() {
        eventoDTO.setDate(LocalDateTime.of(2022, 7, 28, 12, 00));
        eventoDTO.setDateFinal(LocalDateTime.of(2022, 7, 28, 18, 00));
        evento.setDate(LocalDateTime.of(2022, 7, 28, 10, 00));
        evento.setDateFinal(LocalDateTime.of(2022, 7, 28, 11, 00));
        assertThrows(ValidEventException.class, () -> {
            eventoService.saveEvento(eventoDTO);
        });
    }

    @Test // teste da validação checkDate cenario 05
    public void deveRetornarErroAoPersistitUmEventoComDatasMuitoDistantes() {
        eventoDTO.setDate(LocalDateTime.of(2022, 7, 28, 12, 00));
        eventoDTO.setDateFinal(LocalDateTime.of(2022, 7, 29, 17, 00));
        evento.setDate(LocalDateTime.of(2022, 7, 28, 10, 00));
        evento.setDateFinal(LocalDateTime.of(2022, 7, 28, 11, 00));
        assertThrows(ValidEventException.class, () -> {
            eventoService.saveEvento(eventoDTO);
        });
    }

    @Test
    public void deveRetornarErroAoRemoverUmEventoQueNaoExiste() {
        Assertions.assertThrows(ExistsEventoException.class, () -> {
            eventoService.delete(evento.getId());
        });
    }

    @Test
    public void deveRetornarErroAoPersistirUmEventoComNomeInvalido() {
        eventoDTO.setName("te");
        assertThrows(ValidEventException.class, () -> {
            eventoService.saveEvento(eventoDTO);
        });
    }

    @Test
    public void deveRetornarErroAoBuscarUmEventoQueNaoExiste() {
        eventoOptional = Optional.empty();
        when(eventoRepository.findByNameIgnoreCase(evento.getName())).thenReturn(eventoOptional);
        assertThrows(ExistsEventoException.class, () -> {
            eventoService.findByEventoName(evento.getName());
        });
    }


}
//package com.projuris.projetoStag.evento;
//
//import com.projuris.projetoStag.dtos.EventoDTO;
//import com.projuris.projetoStag.entities.Chamber;
//import com.projuris.projetoStag.entities.Evento;
//import com.projuris.projetoStag.helper.EventoMockHelper;
//import com.projuris.projetoStag.repositories.EventoRepository;
//import com.projuris.projetoStag.services.EventoService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.modelmapper.ModelMapper;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.time.Clock;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//
//public class EventoServiceUnitTest{
//
//    EventoService eventoService;
//
//    @Mock
//    private static EventoRepository eventoRepository;
//
//    @Mock
//    private static ModelMapper modelMapper;
//
//    @Mock
//    private static Clock clock;
//
//    private static ZonedDateTime NOW = ZonedDateTime.of(
//            2022,
//            7,
//            28,
//            8,
//            00,
//            00,
//            00,
//            ZoneId.of("GMT")
//
//    );
//
//    private EventoDTO eventoDTO;
//
//    private Evento evento;
//
//    private List<Evento> list;
//
//    Optional<Evento> eventoOptional;
//    @BeforeEach
//    void setUp(){
//        MockitoAnnotations.openMocks(this);
//        eventoService = new EventoService(eventoRepository, modelMapper, clock);
//        when(clock.getZone()).thenReturn(NOW.getZone());
//        when(clock.instant()).thenReturn(NOW.toInstant());
//
//        this.eventoDTO = EventoMockHelper.createEventoDTO();
//        this.evento = EventoMockHelper.createEvento();
//
//        list = Collections.singletonList(evento);
//        eventoOptional = Optional.of(evento);
//        when(eventoRepository.findAll()).thenReturn(list);
//        when(eventoRepository.save(ArgumentMatchers.any(Evento.class))).thenReturn(evento);
//    }
//
//    @Test // todas as validações corretas
//    public void devePersistitUmEvento() throws Exception {
//        eventoDTO.setDate(LocalDateTime.of(2022, 7, 28, 9, 00));
//        eventoDTO.setDateFinal(LocalDateTime.of(2022, 7, 28, 10, 00));
//        evento.setDate(LocalDateTime.of(2022, 7, 28, 8, 00));
//        evento.setDateFinal(LocalDateTime.of(2022, 7, 28, 8, 30));
//        when(eventoRepository.findById(ArgumentMatchers.eq(eventoDTO.getId()))).thenReturn(eventoOptional);
//        ResponseEntity<Object> result = eventoService.saveEvento(eventoDTO);
//        verify(eventoRepository, Mockito.times(1)).save(ArgumentMatchers.any(Evento.class));
//        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
//    }
//
//    @Test // teste da validação existsEvento cenario 01
//    public void deveRetornarErroAoPersistitUmEventoComDataInicialEtiverDentroDeOutroEvento() throws Exception{
//        eventoDTO.setDate(LocalDateTime.of(2022,7,28, 8, 15));
//        eventoDTO.setDateFinal(LocalDateTime.of(2022,7,28, 8, 45));
//        evento.setDate(LocalDateTime.of(2022,7,28, 8, 00));
//        evento.setDateFinal(LocalDateTime.of(2022,7,28, 8, 30));
//        ResponseEntity<Object> result = eventoService.saveEvento(eventoDTO);
//        Mockito.verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
//        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
//    }
//
//    @Test // teste da validação existsEvento cenario 02
//    public void deveRetornarErroAoPersistitUmEventoComDataFinalEtiverDentroDeOutroEvento() throws Exception {
//        eventoDTO.setDate(LocalDateTime.of(2022,7,28, 8, 00));
//        eventoDTO.setDateFinal(LocalDateTime.of(2022,7,28, 8, 45));
//        evento.setDate(LocalDateTime.of(2022,7,28, 8, 30));
//        evento.setDateFinal(LocalDateTime.of(2022,7,28, 9, 00));
//        ResponseEntity<Object> result = eventoService.saveEvento(eventoDTO);
//        verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
//        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
//    }
//
//    @Test // teste da validação existsEvento cenario 03
//    public void deveRetornarErroAoPersistitUmEventoComDatasDeUmEventoExistente() throws Exception {
//        eventoDTO.setDate(LocalDateTime.of(2022,7,28, 8, 00));
//        eventoDTO.setDateFinal(LocalDateTime.of(2022,7,28, 8, 45));
//        evento.setDate(LocalDateTime.of(2022,7,28, 8, 00));
//        evento.setDateFinal(LocalDateTime.of(2022,7,28, 8, 45));
//        ResponseEntity<Object> result = eventoService.saveEvento(eventoDTO);
//        verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
//        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
//    }
//    @Test // teste da validação existsEvento cenario 04
//    public void deveRetornarErroAoPersistitUmEventoComDatasEntreUmEventoExistente() throws Exception {
//        eventoDTO.setDate(LocalDateTime.of(2022,7,28, 8, 00));
//        eventoDTO.setDateFinal(LocalDateTime.of(2022,7,28, 9, 00));
//        evento.setDate(LocalDateTime.of(2022,7,28, 8, 10));
//        evento.setDateFinal(LocalDateTime.of(2022,7,28, 8,50 ));
//        ResponseEntity<Object> result = eventoService.saveEvento(eventoDTO);
//        verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
//        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
//    }
//
//    @Test // teste da validação  existsEvento cenario 05
//    public void deveRetornarErroAoPersistitUmEventoComNomeInvalido() throws Exception {
//        eventoDTO.setName("");
//        eventoDTO.setDate(LocalDateTime.of(2022,7,28, 9, 00));
//        eventoDTO.setDateFinal(LocalDateTime.of(2022,7,28, 9, 30));
//        evento.setDate(LocalDateTime.of(2022,7,28, 10, 00));
//        evento.setDateFinal(LocalDateTime.of(2022,7,28, 11,00 ));
//        ResponseEntity<Object> result = eventoService.saveEvento(eventoDTO);
//        verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
//        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
//    }
//
//    @Test // teste da validação checkDate cenario 01
//    public void deveRetornarErroAoPersistitUmEventoComDataFinalAnteriorDaDataInicial() throws Exception {
//        eventoDTO.setDate(LocalDateTime.of(2022,7,28, 9, 00));
//        eventoDTO.setDateFinal(LocalDateTime.of(2022,7,28, 8, 00));
//        evento.setDate(LocalDateTime.of(2022,7,28, 9, 00));
//        evento.setDateFinal(LocalDateTime.of(2022,7,28, 10,00 ));
//        ResponseEntity<Object> result = eventoService.saveEvento(eventoDTO);
//        verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
//        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
//    }
//
//    @Test // teste da validação checkDate cenario 02
//    public void deveRetornarErroAoPersistitUmEventoComDataInicialAnteriorDaDataAtual() throws Exception {
//        eventoDTO.setDate(LocalDateTime.of(2022,7,28, 7, 59));
//        eventoDTO.setDateFinal(LocalDateTime.of(2022,7,28, 9, 00));
//        evento.setDate(LocalDateTime.of(2022,7,28, 9, 00));
//        evento.setDateFinal(LocalDateTime.of(2022,7,28, 10,00 ));
//        ResponseEntity<Object> result = eventoService.saveEvento(eventoDTO);
//        verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
//        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
//    }
//
//    @Test // teste da validação checkDate cenario 03
//    public void deveRetornarErroAoPersistitUmEventoComDataInicialIgualDataFinal() throws Exception {
//        eventoDTO.setDate(LocalDateTime.of(2022,7,28, 9, 00));
//        eventoDTO.setDateFinal(LocalDateTime.of(2022,7,28, 9, 00));
//        evento.setDate(LocalDateTime.of(2022,7,28, 10, 00));
//        evento.setDateFinal(LocalDateTime.of(2022,7,28, 11,00 ));
//        ResponseEntity<Object> result = eventoService.saveEvento(eventoDTO);
//        verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
//        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
//    }
//
//    @Test // teste da validação checkDate cenario 04
//    public void deveRetornarErroAoPersistitUmEventoComDataExedendoTempoLimite() throws Exception {
//        eventoDTO.setDate(LocalDateTime.of(2022,7,28, 12, 00));
//        eventoDTO.setDateFinal(LocalDateTime.of(2022,7,28, 18, 00));
//        evento.setDate(LocalDateTime.of(2022,7,28, 10, 00));
//        evento.setDateFinal(LocalDateTime.of(2022,7,28, 11,00 ));
//        ResponseEntity<Object> result = eventoService.saveEvento(eventoDTO);
//        verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
//        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
//    }
//
//    @Test // teste da validação checkDate cenario 05
//    public void deveRetornarErroAoPersistitUmEventoComDatasMuitoDistantes() throws Exception {
//        eventoDTO.setDate(LocalDateTime.of(2022,7,28, 12, 00));
//        eventoDTO.setDateFinal(LocalDateTime.of(2022,7,29, 17, 00));
//        evento.setDate(LocalDateTime.of(2022,7,28, 10, 00));
//        evento.setDateFinal(LocalDateTime.of(2022,7,28, 11,00 ));
//        ResponseEntity<Object> result = eventoService.saveEvento(eventoDTO);
//        verify(eventoRepository, Mockito.never()).save(ArgumentMatchers.any());
//        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
//    }
//
//    @Test
//    public void deveAtualizarUmEvento() throws Exception{
//        eventoDTO.setName("TesteDoTest");
//        eventoDTO.setDate(LocalDateTime.of(2022,7,28, 8, 30));
//        eventoDTO.setDateFinal(LocalDateTime.of(2022,7,28, 9, 00));
//        evento.setDate(LocalDateTime.of(2022,7,28, 8, 00));
//        evento.setDateFinal(LocalDateTime.of(2022,7,28, 10,00 ));
//        when(eventoRepository.findById(ArgumentMatchers.eq(evento.getId()))).thenReturn(eventoOptional);
//        ResponseEntity<Object> result = eventoService.updateEvento(evento.getId(), eventoDTO);
//        Assertions.assertEquals(eventoOptional.get().getName(), eventoDTO.getName());
//        Assertions.assertEquals(eventoOptional.get().getDate(), eventoDTO.getDate());
//        Assertions.assertEquals(eventoOptional.get().getDateFinal(), eventoDTO.getDateFinal());
//        Assertions.assertEquals(eventoOptional.get().getChamber(), eventoDTO.getChamber());
//        Assertions.assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
//    }
//    @Test
//    public void deveRetornarUmaListaDeEventos() throws Exception {
//        PageRequest pageRequest = PageRequest.of(0, 10);
//        Page<Evento> lista = new PageImpl<Evento>(list);
//        when(eventoRepository.findAll(ArgumentMatchers.eq(pageRequest))).thenReturn(lista);
//        ResponseEntity<Object> result = eventoService.findAll(pageRequest);
//        verify(eventoRepository, Mockito.times(1)).findAll(ArgumentMatchers.any(PageRequest.class));
//        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
//    }
//
//    @Test
//    public void deveRetornarErroAoBuscarUmEventoQueNaoExiste()throws Exception {
//        when(eventoRepository.existsById(anyLong())).thenReturn(Boolean.FALSE);
//        ResponseEntity<Object> result = eventoService.findByEventoId(anyLong());
//        verify(eventoRepository, Mockito.never()).findBy(ArgumentMatchers.any(), ArgumentMatchers.any());
//        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
//    }
//
//    @Test
//    public void deveRetornarUmEvento() throws Exception {
//        when(eventoRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
//        Evento evento = Mockito.mock(Evento.class);
//        Optional<Evento> eventoOptional = Optional.of(evento);
//        when(eventoRepository.findById(anyLong())).thenReturn(eventoOptional);
//        ResponseEntity<Object> result = eventoService.findByEventoId(anyLong());
//        verify(eventoRepository, Mockito.times(1)).findById(ArgumentMatchers.any(Long.class));
//        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
//    }
//    @Test
//    public void deveRemoverUmEvento() throws Exception {
//        when(eventoRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
//        ResponseEntity<Object> result = eventoService.delete(anyLong());
//        verify(eventoRepository, Mockito.times(1)).deleteById(anyLong());
//        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
//    }
//
//    @Test
//    public void deveRetornarErroAoRemoverUmEventoQueNaoExiste() throws Exception {
//        when(eventoRepository.existsById(anyLong())).thenReturn(Boolean.FALSE);
//        ResponseEntity<Object> result = eventoService.delete(anyLong());
//        verify(eventoRepository, Mockito.never()).deleteById(anyLong());
//        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
//    }
//}

package com.projuris.projetoStag.serviceTest;

import com.projuris.projetoStag.config.ApplicationConfigTest;
import com.projuris.projetoStag.repositories.EventoRepository;
import com.projuris.projetoStag.services.EventoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Clock;

@DisplayName("EventoServiceUnitTest")
public class EventoServiceUnitTest extends ApplicationConfigTest {

    @Autowired
    EventoService eventoService;

    @MockBean
    private static EventoRepository eventoRepository;

    @MockBean
    private static ModelMapper modelMapper;

    @MockBean
    private static Clock clock;

    @Test
    public void deveRemoverUmEvento(){
        Long eventoId = 1L;
        Mockito.when(eventoRepository.existsById(ArgumentMatchers.eq(eventoId))).thenReturn(true);
        eventoService.delete(eventoId);
        Mockito.verify(eventoRepository, Mockito.times(1)).deleteById(ArgumentMatchers.any(Long.class));
    }

    @Test
    public void deveRetornarErroAoRemoverUmEventoQueNaoExiste(){
        Long eventoId = 1L;
        Mockito.when(eventoRepository.existsById(ArgumentMatchers.eq(eventoId))).thenReturn(false);
        eventoService.delete(eventoId);
        Mockito.verify(eventoRepository, Mockito.times(0)).deleteById(ArgumentMatchers.any(Long.class));
    }
}

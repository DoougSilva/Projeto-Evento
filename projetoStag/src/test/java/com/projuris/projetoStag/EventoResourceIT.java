package com.projuris.projetoStag;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.projuris.projetoStag.dtos.EventoDTO;
import com.projuris.projetoStag.entities.Chamber;
import com.projuris.projetoStag.entities.Evento;
import com.projuris.projetoStag.repositories.EventoRepository;
import com.projuris.projetoStag.resources.EventoResource;
import com.projuris.projetoStag.services.EventoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest(classes = FixedClockConfig.class)
@Transactional
public class EventoResourceIT {

    @Autowired
    EventoService eventoService;

    @Autowired
    EventoRepository eventoRepository;

    private ObjectMapper mapper;
    private MockMvc mockMvc;

    private EventoDTO eventoDTO;

    private Evento evento;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        mockMvc =  MockMvcBuilders.standaloneSetup(new EventoResource(eventoService)).build();

        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);

        this.evento = Evento.builder()
                .name("Test")
                .date(LocalDateTime.of(2022, 7, 29, 9, 00))
                .dateFinal(LocalDateTime.of(2022, 7, 29, 10, 00))
                .chamber(Chamber.SALA_1)
                .build();

        this.eventoDTO = EventoDTO.builder()
                .name("Test")
                .date(LocalDateTime.of(2022, 7, 29, 9, 00))
                .dateFinal(LocalDateTime.of(2022, 7, 29, 10, 00))
                .chamber(Chamber.SALA_1)
                .build();

    }

    @AfterEach
    public void tearDown(){
        eventoRepository.deleteAll();
    }

    @Test
    public void testInsert() throws Exception {
        tearDown();
        mockMvc.perform(post("/evento")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsBytes(eventoDTO)))
                        .andExpect(status().isCreated());
        Assertions.assertEquals(1L, eventoRepository.findAll().size());
    }

    @Test
    public void testDelete() throws Exception{
        tearDown();
        eventoRepository.save(this.evento);
        mockMvc.perform(delete("/evento/id/{id}", this.evento.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isOk());
        Assertions.assertEquals(0L, eventoRepository.findAll().size());
    }

    @Test
    public void testUpdate() throws Exception{
        Evento eventoOptional = eventoRepository.save(this.evento);
        EventoDTO eventoUpdate = new EventoDTO(eventoRepository.findById(eventoOptional.getId()));
        eventoUpdate.setName("TestDoTest");
        mockMvc.perform(put("/evento/id/{id}", eventoUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsBytes(eventoUpdate)))
                        .andExpect(status().isAccepted());
        Evento updateOptional = eventoRepository.findById(eventoUpdate.getId()).get();
        assertThat(updateOptional.getName()).isEqualTo("TestDoTest");
    }

    @Test
    public void testFindId() throws Exception{
        Evento eventoOptional = eventoRepository.save(this.evento);
        mockMvc.perform(get("/evento/id/{id}", eventoOptional.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void testFindAll() throws Exception{
        tearDown();
        eventoRepository.save(this.evento);
        eventoRepository.save(this.evento);
        mockMvc.perform(get("/evento?size=1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(jsonPath("$.totalElements", notNullValue()))
                        .andExpect(jsonPath("$.totalPages", notNullValue()));

    }
}

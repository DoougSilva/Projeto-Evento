package com.projuris.projetoStag.evento;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.projuris.projetoStag.ConfigTest.FixedClockConfig;
import com.projuris.projetoStag.dtos.EventoDTO;
import com.projuris.projetoStag.entities.Evento;
import com.projuris.projetoStag.helper.EventoMockHelper;
import com.projuris.projetoStag.repositories.EventoRepository;
import com.projuris.projetoStag.resources.EventoResource;
import com.projuris.projetoStag.services.EventoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest(classes = FixedClockConfig.class)
@ExtendWith(MockitoExtension.class)
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
        mockMvc =  MockMvcBuilders.standaloneSetup(new EventoResource(eventoService)).build();

        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);

        this.evento = EventoMockHelper.createEvento();
        this.eventoDTO = EventoMockHelper.createEventoDTO();

    }


    @Test
    public void testInsert() throws Exception {
        eventoRepository.deleteAll();
        mockMvc.perform(post("/evento")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsBytes(eventoDTO)))
                        .andExpect(status().isCreated());
        Assertions.assertEquals(1L, eventoRepository.findAll().size());
    }

    @Test
    public void testDelete() throws Exception{
        eventoRepository.save(this.evento);
        mockMvc.perform(delete("/evento/del-id/{id}", this.evento.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isOk());
        Assertions.assertEquals(0L, eventoRepository.findAll().size());
    }

    @Test
    public void testUpdate() throws Exception{
        Evento eventoOptional = eventoRepository.save(this.evento);
        EventoDTO eventoUpdate = new EventoDTO(eventoRepository.findById(eventoOptional.getId()));
        eventoUpdate.setName("TestDoTest");
        mockMvc.perform(put("/evento/put-id/{id}", eventoUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsBytes(eventoUpdate)))
                        .andExpect(status().isAccepted());
        Evento updateOptional = eventoRepository.findById(eventoUpdate.getId()).get();
        assertThat(updateOptional.getName()).isEqualTo("TestDoTest");
    }

    @Test
    public void testFindByName() throws Exception{
        eventoRepository.save(this.evento);
        eventoRepository.save(this.evento);
        Evento eventoOptional = eventoRepository.save(this.evento);
        mockMvc.perform(get("/evento/name/{name}", eventoOptional.getName())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(jsonPath("$.totalElements", notNullValue()))
                        .andExpect(jsonPath("$.totalPages", notNullValue()));
    }

    @Test
    public void testFindByChamber() throws Exception{
        eventoRepository.save(this.evento);
        eventoRepository.save(this.evento);
        Evento eventoOptional = eventoRepository.save(this.evento);
        mockMvc.perform(get("/evento/chamber/{chamber}", eventoOptional.getChamber().getCode())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(jsonPath("$.totalElements", notNullValue()))
                        .andExpect(jsonPath("$.totalPages", notNullValue()));
    }

    @Test
    public void testFindAll() throws Exception{
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

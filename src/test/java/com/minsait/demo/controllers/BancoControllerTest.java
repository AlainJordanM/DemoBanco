package com.minsait.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minsait.demo.models.Banco;
import com.minsait.demo.models.Cuenta;
import com.minsait.demo.repositories.BancoRepository;
import com.minsait.demo.repositories.CuentaRepository;
import com.minsait.demo.services.BancoService;
import com.minsait.demo.services.CuentaService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@WebMvcTest(BancoController.class)
class BancoControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BancoService service;
    @MockBean
    private BancoRepository repository;

    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper= new ObjectMapper();
    }

    @Test
    void ListarBancos() throws Exception{
        //give
        Banco banco = new Banco();
        banco.setId(1L);
        banco.setTotalTransferencias(0);
        banco.setNombre("Banco Azteca");
        when(service.findAll()).thenReturn(List.of(banco));
        //when
        mvc.perform(get("/api/bancos/listar").contentType(MediaType.APPLICATION_JSON))
        //then
                .andExpect(status().isOk())//validar que respuesta sea ok
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))//validar que me regrese un Json
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[0].nombre").value("Banco Azteca"))
                .andExpect(jsonPath("$.[0].totalTransferencias").value("0"))
        ;
    }
    @Test
    void ListarBancosPorId() throws Exception{
        //give
        Banco banco = new Banco();
        banco.setId(1L);
        banco.setTotalTransferencias(0);
        banco.setNombre("Banco Azteca");
        when(service.findById(1L)).thenReturn(banco);
        //when
        mvc.perform(get("/api/bancos/listar/1").contentType(MediaType.APPLICATION_JSON))
        //then
                .andExpect(status().isOk())//validar que respuesta sea ok
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))//validar que me regrese un Json
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.nombre").value("Banco Azteca"))
                .andExpect(jsonPath("$.totalTransferencias").value("0"))
        ;
    }
    @Test
    void guardar() throws Exception{
        Banco banco = new Banco();
        banco.setId(1L);
        banco.setTotalTransferencias(0);
        banco.setNombre("Banco Azteca");
        when(service.save(any())).then(invocationOnMock -> {
            Banco banco1 = invocationOnMock.getArgument(0);
            banco1.setId(1L);
            return banco1;
        });
        mvc.perform(post("/api/bancos").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(banco)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre", Matchers.is("Banco Azteca")))
                .andExpect(jsonPath("$.totalTransferencias").value("0"))
        ;
    }
    @Test
    void ListarBancosPorIdException() throws Exception{
        //give
        Banco banco = new Banco();
        banco.setId(1L);
        banco.setTotalTransferencias(0);
        banco.setNombre("Banco Azteca");
        when(service.findById(2L)).thenThrow(NoSuchElementException.class);
        //when
        mvc.perform(get("/api/bancos/listar/2").contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isNotFound())//validar que respuesta sea ok
        ;
    }
}
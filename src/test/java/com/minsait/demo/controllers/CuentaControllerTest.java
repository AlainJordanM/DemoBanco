package com.minsait.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minsait.demo.exception.DineroInsuficienteException;
import com.minsait.demo.models.Cuenta;
import com.minsait.demo.models.TransferirDTO;
import com.minsait.demo.repositories.CuentaRepository;
import com.minsait.demo.services.CuentaService;
import lombok.Data;
import org.apache.logging.log4j.message.Message;
import org.h2.value.Transfer;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuentaController.class)
class CuentaControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CuentaService service;
    @MockBean
    private CuentaRepository repository;

    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper= new ObjectMapper();
    }
    @Test
    void testListar() throws Exception{
        when(service.findAll()).thenReturn(List.of(Datos.crearCuenta1().get(),Datos.crearCuenta2().get()));
        mvc.perform(get("/api/cuentas/listar").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())//validar que respuesta sea ok
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))//validar que me regrese un Json
                .andExpect(jsonPath("$.[0].persona").value("Jordan"))
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[0].saldo").value("1000"))
                .andExpect(jsonPath("$.[1].persona").value("Alain"))
                .andExpect(jsonPath("$.[1].id").value("2"))
                .andExpect(jsonPath("$.[1].saldo").value("5000000"))
        ;

    }
    @Test
    void testListarId() throws Exception {
        when(service.findById(1L)).thenReturn(Datos.crearCuenta1().orElseThrow());

        mvc.perform(get("/api/cuentas/listar/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.persona").value("Jordan"))
                .andExpect(jsonPath("$.saldo").value("1000"))
                ;
        verify(service).findById(1L);
    }
    @Test
    void testGuardar() throws Exception {
        Cuenta cuenta = new Cuenta(null,"Yael", new BigDecimal("1000000"));
        when(service.save(any())).then(invocationOnMock -> {
            Cuenta cuenta1 = invocationOnMock.getArgument(0);
            cuenta1.setId(3L);
            return cuenta1;
        });
        mvc.perform(post("/api/cuentas").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cuenta)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(3)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.persona", Matchers.is("Yael")))
                .andExpect(jsonPath("$.saldo").value(1000000))
                ;
    }
    @Test
    void testTransferir() throws Exception{
        //give
        TransferirDTO transferirDTO = new TransferirDTO();

        transferirDTO.setCuentaOrigenId(Datos.crearCuenta2().get().getId());
        transferirDTO.setCuentaDestinoId(Datos.crearCuenta1().get().getId());
        transferirDTO.setMonto(new BigDecimal("5000000"));
        transferirDTO.setBancoId(Datos.crearBanco().get().getId());

        doNothing().when(service).transferir(isA(Long.class),isA(Long.class),isA(BigDecimal.class),isA(Long.class));
        //when
        mvc.perform(post("/api/cuentas/transferir").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(transferirDTO)))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        ;
    }
    /*
    @Test
    void testTransferirNull() throws Exception{
        //give
        TransferirDTO transferirDTO = new TransferirDTO();

        transferirDTO.setCuentaOrigenId(Datos.crearCuenta2().get().getId());
        transferirDTO.setCuentaDestinoId(Datos.crearCuenta1().get().getId());
        transferirDTO.setMonto(new BigDecimal("9000000"));
        transferirDTO.setBancoId(Datos.crearBanco().get().getId());

        doNothing().when(service).transferir(isA(Long.class),isA(Long.class),isA(BigDecimal.class),isA(Long.class));

        //when
        mvc.perform(post("/api/cuentas/transferir").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(transferirDTO)))
        //then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        ;
    }*/
    @Test
    void testExceptionyTransferir() throws Exception {
        TransferirDTO dto=new TransferirDTO();
        dto.setCuentaOrigenId(2L);
        dto.setCuentaDestinoId(1L);
        dto.setMonto(new BigDecimal(5000000));
        dto.setBancoId(1L);
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("date", LocalDate.now().toString());
        respuesta.put("transaccion", dto);
        respuesta.put("status", "OK");
        respuesta.put("message", "Transferencia realizada con exito");

        mvc.perform(post("/api/cuentas/transferir").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(respuesta)))
        ;

    }
    /*
    @Test
    void testException() throws Exception {
        when(repository.findById(2L)).thenReturn(Datos.crearCuenta2());
        when(repository.findById(1L)).thenReturn(Datos.crearCuenta1());
        Exception exception = assertThrows(DineroInsuficienteException.class,()->{
           service.transferir(Datos.crearCuenta2().get().getId(), Datos.crearCuenta1().get().getId(), new BigDecimal(6000000),Datos.crearBanco().get().getId());
        });
        String actual = exception.getMessage();
        assertEquals("Dinero Insuficiente", actual);
        doThrow(exception).when(service).transferir(2L,1L,new BigDecimal(6000000),1L);



        TransferirDTO dto=new TransferirDTO();
        dto.setCuentaOrigenId(2L);
        dto.setCuentaDestinoId(1L);
        dto.setMonto(new BigDecimal(6000000));
        dto.setBancoId(1L);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("date", LocalDate.now().toString());
        respuesta.put("transaccion", dto);
        respuesta.put("status", "OK");
        respuesta.put("message", "Dinero insuficiente");

        mvc.perform(post("/api/cuentas/transferir").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(respuesta)))
        ;
    }*/

}
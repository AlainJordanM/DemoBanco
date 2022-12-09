package com.minsait.demo.services;

import com.minsait.demo.models.Banco;
import com.minsait.demo.models.Cuenta;
import com.minsait.demo.repositories.BancoRepository;
import com.minsait.demo.repositories.CuentaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BancoImplTest {

    @Mock
    CuentaRepository cuentaRepository;
    @Mock
    BancoRepository bancoRepository;
    @InjectMocks
    BancoImpl service;
    @Captor
    ArgumentCaptor<Long> captor;

    @Test
    void findAll() {
        //give
        Banco banco = new Banco(1L,"Banco Azteca",0);
        when(bancoRepository.findAll()).thenReturn(List.of(banco));
        //when
        List<Banco> bancoList = service.findAll();
        //then
        assertNotNull(bancoList);
    }

    @Test
    void findById() {
        //give
        Banco banco = new Banco(1L,"Banco Azteca",0);
        when(bancoRepository.findById(1L)).thenReturn(Optional.of(banco));
        //when
        Banco banco1= service.findById(1L);
        //then
        assertEquals(banco1,banco);
    }

    @Test
    void save() {
        //given
        Banco banco = new Banco(1L,"Banco Azteca",0);
        when(bancoRepository.save(banco)).thenReturn(banco);
        //when
        Banco banco1 = service.save(banco);
        //then
        assertNotNull(banco1);
        assertEquals(banco,banco1);
    }
}
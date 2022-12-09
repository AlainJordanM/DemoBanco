package com.minsait.demo.services;

import com.minsait.demo.exception.DineroInsuficienteException;
import com.minsait.demo.models.Banco;
import com.minsait.demo.models.Cuenta;
import com.minsait.demo.models.TransferirDTO;
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
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CuentaServiceImplTest {
    @Mock
    CuentaRepository cuentaRepository;
    @Mock
    BancoRepository bancoRepository;
    @InjectMocks
    CuentaServiceImpl service;
    @Captor
    ArgumentCaptor<Long> captor;
    @Test
    void findAllCuenta() {
        //give
        Cuenta cuenta = new Cuenta(1L,"Jordan",new BigDecimal(5000000));
        when(cuentaRepository.findAll()).thenReturn(List.of(cuenta));
        //when
        List<Cuenta> cuentaList = service.findAll();
        //then
        assertNotNull(cuentaList);
    }

    @Test
    void findById() {
        //give
        Cuenta cuenta = new Cuenta(1L,"Jordan",new BigDecimal(5000000));
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        //when
        Cuenta cuentaOptional= service.findById(1L);
        //then
        assertEquals(cuentaOptional,cuenta);
    }

    @Test
    void revisarTotalTransferencias() {
        //give
        Banco banco = new Banco(1L,"Banco Azteca",0);
        when(bancoRepository.findById(1L)).thenReturn(Optional.of(banco));
        //when
        Integer Tt= service.revisarTotalTransferencias(1L);
        //then
        assertNotNull(Tt);
        assertEquals(0,Tt);
    }

    @Test
    void revisarSaldo() {
        //give
        Cuenta cuenta = new Cuenta(1L,"Jordan",new BigDecimal(500));
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        //when
        BigDecimal rSaldo= service.revisarSaldo(1L);
        //then
        assertNotNull(rSaldo);
        assertEquals(new BigDecimal(500),rSaldo);
    }

    @Test
    void transferir() {
       //give
        TransferirDTO transferirDTO = new TransferirDTO();
        transferirDTO.setCuentaOrigenId(2L);
        transferirDTO.setCuentaDestinoId(1L);
        transferirDTO.setMonto(new BigDecimal(5000));
        transferirDTO.setBancoId(1L);

        Cuenta cuentaO = new Cuenta(2L,"Jordan",new BigDecimal(5000));
        when(cuentaRepository.findById(2L)).thenReturn(Optional.of(cuentaO));

        Cuenta cuentaD = new Cuenta(1L,"Alain",new BigDecimal(100));
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaD));

        Banco banco = new Banco(1L,"Banco Azteca",0);
        when(bancoRepository.findById(1L)).thenReturn(Optional.of(banco));

        //doNothing().when(service).transferir(2L,1L,new BigDecimal(5000),1L);

        //when
        service.transferir(2L,1L,new BigDecimal(5000),1L);
        //then
        verify(cuentaRepository,atLeast(1)).findById(2L);
        verify(cuentaRepository,atLeast(1)).findById(1L);

    }

    @Test
    void save() {
        //given
        Cuenta cuenta = new Cuenta(2L,"Jordan",new BigDecimal(5000));
        when(cuentaRepository.save(cuenta)).thenReturn(cuenta);
        //when
        Cuenta cuenta1 = service.save(cuenta);
        //then
        assertNotNull(cuenta1);
        assertEquals(cuenta,cuenta1);
    }

    @Test
    void TransferirError(){
        //give
        TransferirDTO transferirDTO = new TransferirDTO();
        transferirDTO.setCuentaOrigenId(2L);
        transferirDTO.setCuentaDestinoId(1L);
        transferirDTO.setMonto(new BigDecimal(50000));
        transferirDTO.setBancoId(1L);

        Cuenta cuentaO = new Cuenta(2L,"Jordan",new BigDecimal(5000));
        //when(cuentaRepository.findById(2L)).thenReturn(Optional.of(cuentaO));

        Cuenta cuentaD = new Cuenta(1L,"Alain",new BigDecimal(100));
        //when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaD));

        Banco banco = new Banco(1L,"Banco Azteca",0);
        //when(bancoRepository.findById(1L)).thenReturn(Optional.of(banco));

        //doThrow(IllegalArgumentException.class).when(service).transferir(transferirDTO.getCuentaOrigenId(),transferirDTO.getCuentaDestinoId(),transferirDTO.getMonto(),transferirDTO.getBancoId());

        //when
        assertThrows(NoSuchElementException.class,() -> service.transferir(transferirDTO.getCuentaOrigenId(),transferirDTO.getCuentaDestinoId(),transferirDTO.getMonto(),transferirDTO.getBancoId()));
    }
}
package com.minsait.demo;

import com.minsait.demo.models.Cuenta;
import com.minsait.demo.repositories.CuentaRepository;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class IntegrationJpaTest {
    @Autowired
    CuentaRepository repository;

    @Test
    void testFindById(){
        Optional<Cuenta> cuenta = repository.findById(1L);
        assertTrue(cuenta.isPresent());
        assertEquals("Jordan",cuenta.get().getPersona());
    }

    @Test
    void testFindByPersona(){
        Optional<Cuenta> cuenta = repository.findByPersona("Jordan");
        assertTrue(cuenta.isPresent());
        assertEquals("Jordan",cuenta.get().getPersona());
    }
    @Test
    void testFindByPersonaException(){
        Optional<Cuenta> cuenta = repository.findByPersona("Yael");
        assertThrows(NoSuchElementException.class, cuenta::orElseThrow);
        assertFalse(cuenta.isPresent());
    }
    @Test
    void testSave(){
        Cuenta cuenta = new Cuenta(null,"Yael", new BigDecimal(1000000));
        Cuenta cuenta1 = repository.save(cuenta);

        assertEquals("Yael", cuenta.getPersona());
        assertEquals("1000000", cuenta.getSaldo().toPlainString());
    }
    @Test
    void testUpdate(){
        Cuenta cuenta = new Cuenta(null,"Yael", new BigDecimal(1000000));
        Cuenta cuenta1 = repository.save(cuenta);

        assertEquals("Yael", cuenta.getPersona());
        assertEquals("1000000", cuenta.getSaldo().toPlainString());

        cuenta1.setSaldo(new BigDecimal(10000));
        Cuenta cuentaActualizada = repository.save(cuenta1);

        assertEquals("Yael", cuentaActualizada.getPersona());
        assertEquals("10000", cuentaActualizada.getSaldo().toPlainString());
    }
    @Test
    void testDelete(){
        Cuenta cuenta = repository.findById(1L).orElseThrow();
        assertEquals("Jordan", cuenta.getPersona());

        repository.delete(cuenta);

        assertThrows(NoSuchElementException.class, () -> {
            repository.findById(1L).orElseThrow();
        });

    }
}

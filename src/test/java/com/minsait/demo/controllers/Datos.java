package com.minsait.demo.controllers;

import com.minsait.demo.models.Banco;
import com.minsait.demo.models.Cuenta;

import java.math.BigDecimal;
import java.util.Optional;

public class Datos {
    public static Optional<Cuenta> crearCuenta1(){
        return Optional.of(new Cuenta(1L,"Jordan", new BigDecimal("1000")));
    }
    public static Optional<Cuenta> crearCuenta2(){
        return Optional.of(new Cuenta(2L,"Alain", new BigDecimal("5000000")));
    }
    public static Optional<Banco> crearBanco(){
        return Optional.of(new Banco(1L,"Banco Azteca", 0));
    }
}

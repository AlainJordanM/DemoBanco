package com.minsait.demo.services;

import com.minsait.demo.models.Banco;
import com.minsait.demo.models.Cuenta;

import java.math.BigDecimal;
import java.util.List;

public interface BancoService {
    List<Banco> findAll();
    Banco findById(Long id);
    Banco save(Banco banco);
}

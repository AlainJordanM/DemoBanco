package com.minsait.demo.services;

import com.minsait.demo.models.Cuenta;

import java.math.BigDecimal;
import java.util.List;

public interface CuentaService {
    List<Cuenta> findAll();
    Cuenta findById(Long id);
    Integer revisarTotalTransferencias(Long bancoId);
    BigDecimal revisarSaldo(Long cuentaId);
    void transferir(Long cuentaOrigen, Long cuentaDestino, BigDecimal monto, Long bancoId);
    Cuenta save(Cuenta cuenta);
}

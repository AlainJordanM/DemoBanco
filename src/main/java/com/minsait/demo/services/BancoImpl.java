package com.minsait.demo.services;

import com.minsait.demo.models.Banco;
import com.minsait.demo.models.Cuenta;
import com.minsait.demo.repositories.BancoRepository;
import com.minsait.demo.repositories.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class BancoImpl implements BancoService{


    @Autowired
    private BancoRepository bancoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Banco> findAll() {
        return bancoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Banco findById(Long id) {
        return bancoRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public Banco save(Banco banco) {
        return bancoRepository.save(banco);
    }

}

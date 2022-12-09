package com.minsait.demo.controllers;

import com.minsait.demo.models.Banco;
import com.minsait.demo.models.Cuenta;
import com.minsait.demo.services.BancoService;
import com.minsait.demo.services.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
// /api/cuentas
@RequestMapping("/api/bancos")
public class BancoController {
    @Autowired
    private BancoService service;

    @GetMapping("/listar")
    @ResponseStatus(HttpStatus.OK)
    public List<Banco> findAll(){
        return service.findAll();
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<Banco> findById(@PathVariable Long id){
        try {
            Banco banco = service.findById(id);
            return ResponseEntity.ok(banco);
        }catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Banco guardar(@RequestBody Banco banco){
        return service.save(banco);
    }
}

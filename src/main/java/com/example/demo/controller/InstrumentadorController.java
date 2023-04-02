package com.example.demo.controller;

import com.example.demo.dto.instrumentacion.Cuenta;
import com.example.demo.dto.instrumentacion.Integracion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class InstrumentadorController {
    @PostMapping("/cuentas")
    public Cuenta crearCuenta(@RequestBody Cuenta cuenta) {
        log.info("{}", cuenta);

        return hacerAlgoConLa(cuenta);
    }

    private static Cuenta hacerAlgoConLa(Cuenta cuenta) {
        cuenta.setNumero(1);

        long counter = 1;

        for (Integracion integracion : cuenta.getIntegraciones()) {
            integracion.getPersona().setId(counter++);
        }

        return cuenta;
    }
}

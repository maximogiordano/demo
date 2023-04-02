package com.example.demo.controller;

import com.example.demo.client.personas.PersonasClient;
import com.example.demo.client.personas.dto.PersonaDto;
import com.example.demo.client.personas.dto.PersonaFisicaDto;
import com.example.demo.client.personas.dto.PersonaJuridicaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    public static final String CANAL = "BATCH";
    public static final String USUARIO = "MG12321";

    @Autowired
    private PersonasClient personasClient;

    @Value("${client.id}")
    private String clientId;

    @GetMapping("/fisicas/{id}")
    public PersonaFisicaDto obtenerPersonaFisica(@PathVariable Long id) {
        return personasClient.obtenerPersonaFisica(CANAL, USUARIO, clientId, id);
    }

    @GetMapping("/juridicas/{id}")
    public PersonaJuridicaDto obtenerPersonaJuridica(@PathVariable Long id) {
        return personasClient.obtenerPersonaJuridica(CANAL, USUARIO, clientId, id);
    }

    @GetMapping("/personas/{id}")
    public PersonaDto obtenerPersona(@PathVariable Long id) {
        return personasClient.obtenerPersona(CANAL, USUARIO, clientId, id);
    }
}

package com.example.demo.client.personas;

import com.example.demo.client.personas.dto.PersonaDto;
import com.example.demo.client.personas.dto.PersonaFisicaDto;
import com.example.demo.client.personas.dto.PersonaJuridicaDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "${client.personas.name}", url = "${client.personas.url}")
public interface PersonasClient {
    @GetMapping("/v2.0/fisicas/{id}")
    PersonaFisicaDto obtenerPersonaFisica(
            @RequestHeader("X-Canal") String canal,
            @RequestHeader("X-Usuario") String usuario,
            @RequestHeader("X-IBM-Client-Id") String clientId,
            @PathVariable("id") Long id
    );

    @GetMapping("/v2.0/juridicas/{id}")
    PersonaJuridicaDto obtenerPersonaJuridica(
            @RequestHeader("X-Canal") String canal,
            @RequestHeader("X-Usuario") String usuario,
            @RequestHeader("X-IBM-Client-Id") String clientId,
            @PathVariable("id") Long id
    );

    @GetMapping("/v2.0/personas/{id}")
    PersonaDto obtenerPersona(
            @RequestHeader("X-Canal") String canal,
            @RequestHeader("X-Usuario") String usuario,
            @RequestHeader("X-IBM-Client-Id") String clientId,
            @PathVariable("id") Long id
    );

    @PostMapping("/v2.0/fisicas")
    PersonaFisicaDto crearPersonaFisica(
            @RequestHeader("X-Canal") String canal,
            @RequestHeader("X-Usuario") String usuario,
            @RequestHeader("X-IBM-Client-Id") String clientId,
            @RequestBody PersonaFisicaDto dto
    );

    @PatchMapping("/v2.0/fisicas/{id}")
    PersonaFisicaDto actualizarPersonaFisica(
            @RequestHeader("X-Canal") String canal,
            @RequestHeader("X-Usuario") String usuario,
            @RequestHeader("X-IBM-Client-Id") String clientId,
            @PathVariable("id") Long id,
            @RequestBody PersonaFisicaDto dto
    );

    @PostMapping("/v2.0/personas/{id}/alta-bt")
    void altaBt(
            @RequestHeader("X-Canal") String canal,
            @RequestHeader("X-Usuario") String usuario,
            @RequestHeader("X-IBM-Client-Id") String clientId,
            @PathVariable("id") Long id
    );
}

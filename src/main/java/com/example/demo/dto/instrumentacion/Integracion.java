package com.example.demo.dto.instrumentacion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Integracion {
    @JsonProperty(value = "codigo_titularidad", access = JsonProperty.Access.WRITE_ONLY)
    private Integer codigoTitularidad;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String firma;

    private Persona persona;
}

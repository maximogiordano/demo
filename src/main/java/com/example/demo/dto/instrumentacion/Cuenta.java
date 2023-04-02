package com.example.demo.dto.instrumentacion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Cuenta {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer numero;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String nombre;

    private List<Integracion> integraciones;
}

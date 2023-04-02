package com.example.demo.client.personas.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DomicilioDto {
    private Long id;
    private Integer pais;
    private Integer provincia;
    private String localidadMaestro;
    private Integer codigoPostal;
    private String calle;
    private String numero;
    private String piso;
    private String departamento;
    private Boolean legal;
}

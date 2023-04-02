package com.example.demo.client.personas.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OcupacionDto {
    private Long id;
    private Integer ocupacion;
    private String detalleOcupacion;
    private LocalDate fechaInicioOcupacion;
    private String empresa;
    private Integer vinculoConEmpresa;
    private BigDecimal ingresoMensual;
    private String categoriaAutonomo;
    private String categoriaMonotributo;
    private String categoriaMonotributoSocial;
}

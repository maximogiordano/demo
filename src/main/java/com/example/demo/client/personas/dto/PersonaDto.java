package com.example.demo.client.personas.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PersonaDto {
    private Long id;
    private Integer paisDocumento;
    private Integer tipoDocumento;
    private String numeroDocumento;
    private Integer tipoTributario;
    private String numeroTributario;
    private Integer paisResidencia;
    private Integer canalDistribucion;
    private List<OcupacionDto> ocupaciones;
    private List<DomicilioDto> domicilios;
    private List<EmailDto> emails;
    private List<TelefonoDto> telefonos;
}

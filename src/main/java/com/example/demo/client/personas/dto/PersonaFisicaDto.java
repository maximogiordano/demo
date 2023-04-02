package com.example.demo.client.personas.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PersonaFisicaDto extends PersonaDto {
    private String nombre;
    private String apellido;
    private String genero;
    private Integer paisNacimiento;
    private String lugarNacimiento;
    private LocalDate fechaNacimiento;
    private Integer paisCiudadania;
    private Boolean esCiudadanoLegal;
    private Integer estadoCivil;
}

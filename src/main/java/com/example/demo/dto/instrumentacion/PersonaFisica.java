package com.example.demo.dto.instrumentacion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PersonaFisica extends Persona {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String nombre;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String apellido;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String genero;
}

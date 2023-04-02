package com.example.demo.dto.instrumentacion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PersonaJuridica extends Persona {
    @JsonProperty(value = "razon_social", access = JsonProperty.Access.WRITE_ONLY)
    private String razonSocial;
}

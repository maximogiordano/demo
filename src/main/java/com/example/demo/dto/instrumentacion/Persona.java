package com.example.demo.dto.instrumentacion;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
// Usamos una propiedad adicional llamada "tipo" donde vamos a poner el nombre lógico de la subclase. De esta manera,
// Jackson entiende cómo serializar y de-serializar cualquier tipo de persona.
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tipo")
// Definimos los distintos tipos de persona y los nombres lógicos correspondientes.
@JsonSubTypes({
        @JsonSubTypes.Type(value = PersonaFisica.class, name = "F"),
        @JsonSubTypes.Type(value = PersonaJuridica.class, name = "J")
})
public abstract class Persona {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Documento documento;
}

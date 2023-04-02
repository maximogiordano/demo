package com.example.demo.dto.instrumentacion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Documento {
    @JsonProperty("codigo_pais")
    private Integer codigoPais;

    @JsonProperty("codigo_tipo")
    private Integer codigoTipo;

    private String numero;
}

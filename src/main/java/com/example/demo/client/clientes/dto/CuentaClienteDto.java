package com.example.demo.client.clientes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CuentaClienteDto {
    private Integer numeroCuenta;
    private String nombreCuenta;
    private String codigoEjecutivo;
    private Integer sector;
    private Integer codigoActividadInterno;
    private Integer clasificacionInterna;
    private Integer segmentoComercial;
    private Integer sucursal;
    private LocalDate vencimientoClasificacion;
    private Boolean residente;
    private Boolean cuentaProveedor;
    private Boolean cuentaEmpleado;
    private Boolean retenerCorrespondencia;
    private List<IntegracionDto> integraciones;
}

package com.example.demo.client.clientes;

import com.example.demo.client.clientes.dto.CuentaClienteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "${client.clientes.name}", url = "${client.clientes.url}")
public interface ClientesClient {
    @GetMapping("/v1.0/cuentas/{numero-cuenta}")
    CuentaClienteDto obtenerCuentaCliente(
            @RequestHeader("X-Canal") String canal,
            @RequestHeader("X-Usuario") String usuario,
            @RequestHeader("X-IBM-Client-Id") String clientId,
            @PathVariable("numero-cuenta") Integer numeroCuenta
    );

    @PostMapping("/v1.0/cuentas")
    CuentaClienteDto crearCuentaCliente(
            @RequestHeader("X-Canal") String canal,
            @RequestHeader("X-Usuario") String usuario,
            @RequestHeader("X-IBM-Client-Id") String clientId,
            @RequestBody CuentaClienteDto dto
    );

    @PatchMapping("/v1.0/cuentas/{numero-cuenta}")
    CuentaClienteDto actualizarCuentaCliente(
            @RequestHeader("X-Canal") String canal,
            @RequestHeader("X-Usuario") String usuario,
            @RequestHeader("X-IBM-Client-Id") String clientId,
            @PathVariable("numero-cuenta") Integer numeroCuenta,
            @RequestBody CuentaClienteDto dto
    );
}

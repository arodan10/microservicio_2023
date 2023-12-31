package com.example.venta.feign;

import com.example.venta.dto.Cliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cliente-service", path = "/cliente")
public interface ClienteFeign {
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getCliente(@PathVariable(required = true) Integer id);
}

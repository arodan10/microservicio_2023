package com.example.venta.feign;

import com.example.venta.dto.Producto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "catalogo-service", path = "/producto")

public interface ProductoFeign {
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProducto(@PathVariable(required = true) Integer id) ;

    @GetMapping("/{id}/stock")
    public ResponseEntity<Producto> updateStockProduct(@PathVariable  Integer id ,@RequestParam(name = "cantidad", required = true) Double cantidad);
}

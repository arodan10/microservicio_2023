package com.example.venta.controller;

import com.example.venta.entity.Venta;
import com.example.venta.service.VentaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/venta")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public ResponseEntity<List<Venta>> listAllVentas() {
        List<Venta> ventas = ventaService.listar();
        if (ventas.isEmpty()) {
            return  ResponseEntity.noContent().build();
        }
        return  ResponseEntity.ok(ventas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> getVentas(@PathVariable("id") int id) {
        log.info("Fetching Invoice with id {}", id);
        Venta venta  = ventaService.listarPorId(id);
        if (null == venta) {
            log.error("Invoice with id {} not found.", id);
            return  ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(venta);
    }

    @PostMapping
    public ResponseEntity<Venta> createVenta(@Valid @RequestBody Venta venta, BindingResult result) {
        log.info("Creating Invoice : {}", venta);
        if (result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        Venta ventaDB = ventaService.guardar (venta);

        return  ResponseEntity.status( HttpStatus.CREATED).body(ventaDB);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venta> updateVenta(@PathVariable(required = true) int id, @RequestBody Venta venta) {
        log.info("Updating Invoice with id {}", id);

        venta.setId(id);
        Venta currentVenta=ventaService.actualizar(venta);

        if (currentVenta == null) {
            log.error("Unable to update. Invoice with id {} not found.", id);
            return  ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(currentVenta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Venta> deleteVenta(@PathVariable(required = true) int id) {
        log.info("Fetching & Deleting Invoice with id {}", id);

        Venta venta = ventaService.listarPorId(id);
        if (venta == null) {
            log.error("Unable to delete. Invoice with id {} not found.", id);
            return  ResponseEntity.notFound().build();
        }
        venta = ventaService.eliminar(venta);
        return ResponseEntity.ok(venta);
    }

    private String formatMessage( BindingResult result){
        List<Map<String,String>> errors = result.getFieldErrors().stream()
                .map(err ->{
                    Map<String,String> error =  new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;

                }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors).build();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString="";
        try {
            jsonString = mapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
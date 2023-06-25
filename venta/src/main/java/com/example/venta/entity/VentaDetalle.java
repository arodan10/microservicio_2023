package com.example.venta.entity;

import com.example.venta.dto.Cliente;
import com.example.venta.dto.Producto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Data
public class VentaDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Positive(message = "El stock debe ser mayor que cero")
    private Double cantidad;
    private Double precio;
    private Integer productoId;

    @Transient
    private Double subTotal;

    @Transient
    private Producto producto;

    public VentaDetalle() { 
        this.cantidad = (double) 0;
        this.precio = (double) 0;
    }

    public Double getSubTotal(){
        if (this.precio >0  && this.cantidad >0 ){
            return this.cantidad * this.precio;
        }else {
            return (double) 0;
        }
    }

}
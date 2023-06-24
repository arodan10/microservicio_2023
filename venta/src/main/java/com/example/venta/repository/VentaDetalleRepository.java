package com.example.venta.repository;

import com.example.venta.entity.VentaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
public interface VentaDetalleRepository extends JpaRepository<VentaDetalle, Integer> {
}

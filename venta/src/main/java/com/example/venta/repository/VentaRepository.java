package com.example.venta.repository;

import com.example.venta.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Integer> {
    public List<Venta> findByClienteId(Integer clienteId );
    public Venta findByNumero(String numero);
}

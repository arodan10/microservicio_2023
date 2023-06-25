package com.example.venta.service.impl;

import com.example.venta.dto.Cliente;
import com.example.venta.dto.Producto;
import com.example.venta.entity.Venta;
import com.example.venta.entity.VentaDetalle;
import com.example.venta.feign.ClienteFeign;
import com.example.venta.feign.ProductoFeign;
import com.example.venta.repository.VentaRepository;
import com.example.venta.service.VentaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VentaServiceImpl implements VentaService {
    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private VentaRepository ventaDetalleRepository;

    @Autowired
    private ClienteFeign clienteFeign;

    @Autowired
    private ProductoFeign productoFeign;

    @Override
    public List<Venta> listar() {
        return  ventaRepository.findAll();
    }


    @Override
    public Venta guardar(Venta venta) {
        Venta ventaDB = ventaRepository.findByNumero ( venta.getNumero () );
        if (ventaDB !=null){
            return  ventaDB;
        }
        ventaDB = ventaRepository.save(venta);
        ventaDB.getDetalle().forEach( ventaDetalle -> {
            productoFeign.updateStockProduct( ventaDetalle.getProductoId(), ventaDetalle.getCantidad() * -1);
        });

        return ventaDB;
    }

    @Override
    public Venta actualizar(Venta venta) {
        Venta ventaDB = listarPorId(venta.getId());
        if (ventaDB == null){
            return  null;
        }
        ventaDB.setClienteId(venta.getClienteId());
        ventaDB.setDescripcion(venta.getDescripcion());
        ventaDB.setNumero(venta.getNumero());
        ventaDB.getDetalle().clear();
        ventaDB.setDetalle(venta.getDetalle());
        return ventaRepository.save(ventaDB);
    }

    @Override
    public Venta listarPorId(Integer id) {

        Venta venta= ventaRepository.findById(id).orElse(null);
        if (null != venta ){
            Cliente cliente = clienteFeign.getCliente(venta.getClienteId()).getBody();
            venta.setCliente(cliente);
            List<VentaDetalle> listDetalle=venta.getDetalle().stream().map(ventaDetalle -> {
                Producto producto = productoFeign.getProducto(ventaDetalle.getProductoId()).getBody();
                ventaDetalle.setProducto(producto);
                return ventaDetalle;
            }).collect(Collectors.toList());
            venta.setDetalle(listDetalle);
        }
        return venta ;
    }

    @Override
    public Venta eliminar(Venta venta) {
        Venta ventaDB = listarPorId(venta.getId());
        if (ventaDB == null){
            return  null;
        }
        return ventaRepository.save(ventaDB);
    }


}

package com.cenfotec.soundwavemusic.services;

import com.cenfotec.soundwavemusic.models.*;
import com.cenfotec.soundwavemusic.repos.CarritoProductoRepository;
import com.cenfotec.soundwavemusic.repos.CarritoRepository;
import com.cenfotec.soundwavemusic.repos.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoProductoRepository carritoProductoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private InventarioService inventarioService;

    public Carrito obtenerOCrearCarritoActivo(Usuario usuario) {
        return carritoRepository.findByUsuarioIdAndEstadoTrue(usuario.getId())
                .orElseGet(() -> {
                    Carrito nuevo = new Carrito();
                    nuevo.setUsuario(usuario);
                    nuevo.setFechaCreacion(LocalDateTime.now());
                    nuevo.setEstado(true);
                    return carritoRepository.save(nuevo);
                });
    }

    public void agregarProducto(Carrito carrito, int idProducto, int cantidad) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Inventario inventario = inventarioService.obtenerPorProductoId(idProducto)
                .orElseThrow(() -> new RuntimeException("No hay inventario para este producto"));

        if (inventario.getCantidadDisponible() < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }

        CarritoProducto cp = new CarritoProducto();
        cp.setCarrito(carrito);
        cp.setProducto(producto);
        cp.setCantidad(cantidad);
        cp.setPrecioUnitario(producto.getPrecio()); // o precio con descuento si aplica

        carritoProductoRepository.save(cp);
    }

    public void eliminarProductoDelCarrito(int idCarritoProducto) {
        carritoProductoRepository.deleteById(idCarritoProducto);
    }
}

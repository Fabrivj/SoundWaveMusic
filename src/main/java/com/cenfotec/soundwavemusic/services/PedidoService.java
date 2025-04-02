package com.cenfotec.soundwavemusic.services;

import com.cenfotec.soundwavemusic.models.Carrito;
import com.cenfotec.soundwavemusic.models.CarritoProducto;
import com.cenfotec.soundwavemusic.models.Pedido;
import com.cenfotec.soundwavemusic.models.Usuario;
import com.cenfotec.soundwavemusic.models.PedidoDetalle;
import com.cenfotec.soundwavemusic.models.Factura;
import com.cenfotec.soundwavemusic.repos.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoDetalleRepository pedidoDetalleRepository;

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private CarritoProductoRepository carritoProductoRepository;

    @Transactional
    public int  generarPedidoDesdeCarrito(int idUsuario, String direccionEnvio) {
        Usuario usuario = usuarioService.obtenerPorId(idUsuario);
        Carrito carrito = carritoRepository.findByUsuarioIdAndEstadoTrue(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Carrito activo no encontrado"));

        List<CarritoProducto> productos = carrito.getProductos();
        if (productos.isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        double subtotal = productos.stream()
                .mapToDouble(p -> p.getPrecioUnitario() * p.getCantidad())
                .sum();

        double impuesto = subtotal * 0.13;
        double total = subtotal + impuesto;

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado(true);
        pedido.setDireccionEnvio(direccionEnvio); // ✅ se usa la dirección enviada por el usuario
        pedido.setTotal(total);
        pedidoRepository.save(pedido);

        for (CarritoProducto cp : productos) {
            PedidoDetalle detalle = new PedidoDetalle();
            detalle.setPedido(pedido);
            detalle.setProducto(cp.getProducto());
            detalle.setCantidad(cp.getCantidad());
            detalle.setPrecioUnitario(cp.getPrecioUnitario());
            detalle.setSubtotal(cp.getPrecioUnitario() * cp.getCantidad());
            pedidoDetalleRepository.save(detalle);
        }

        Factura factura = new Factura();
        factura.setPedido(pedido);
        factura.setFechaEmision(LocalDateTime.now());
        factura.setImpuesto(impuesto);
        factura.setMontoTotal(total);
        facturaRepository.save(factura);

        carrito.setEstado(false);
        carritoRepository.save(carrito);
        carritoProductoRepository.deleteAll(productos);
        return factura.getId();

    }
}

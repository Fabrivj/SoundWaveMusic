package com.cenfotec.soundwavemusic.controllers;


import com.cenfotec.soundwavemusic.models.*;
import com.cenfotec.soundwavemusic.services.CarritoService;
import com.cenfotec.soundwavemusic.services.PedidoService;
import org.springframework.ui.Model;
import com.cenfotec.soundwavemusic.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/carrito")
public class CarritoController {


    @Autowired
    private CarritoService carritoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/ver")
    public String verCarrito(@RequestParam("idUsuario") long idUsuario, Model model) {
        Usuario usuario = usuarioService.obtenerPorId(idUsuario);
        Carrito carrito = carritoService.obtenerOCrearCarritoActivo(usuario);
        List<CarritoProducto> productos = carrito.getProductos();

        double total = productos.stream()
                .mapToDouble(p -> p.getPrecioUnitario() * p.getCantidad())
                .sum();

        model.addAttribute("carrito", carrito);
        model.addAttribute("productos", productos);
        model.addAttribute("total", total);
        return "carrito_ver";
    }

    @PostMapping("/agregar")
    public String agregarProductoAlCarrito(@RequestParam int idUsuario,
                                           @RequestParam int idProducto,
                                           @RequestParam int cantidad,
                                           RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioService.obtenerPorId(idUsuario);
            Carrito carrito = carritoService.obtenerOCrearCarritoActivo(usuario);
            carritoService.agregarProducto(carrito, idProducto, cantidad);

            redirectAttributes.addFlashAttribute("mensaje", "Producto agregado al carrito");
            redirectAttributes.addFlashAttribute("tipoAlerta", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoAlerta", "error");
        }

        return "redirect:/productos/listar";
    }

    @GetMapping("/eliminar")
    public String eliminarProducto(@RequestParam int idCarritoProducto, @RequestParam int idUsuario, RedirectAttributes redirectAttributes) {
        carritoService.eliminarProductoDelCarrito(idCarritoProducto);
        redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado del carrito");
        redirectAttributes.addFlashAttribute("tipoAlerta", "success");
        return "redirect:/carrito/ver?idUsuario=" + idUsuario;
    }

    @PostMapping("/finalizar")
    public String finalizarCompra(@RequestParam int idUsuario,
                                  @RequestParam String direccionEnvio,
                                  RedirectAttributes redirectAttributes) {
        try {
            int idFactura = pedidoService.generarPedidoDesdeCarrito(idUsuario, direccionEnvio);

            redirectAttributes.addFlashAttribute("mensaje", "Compra finalizada con éxito.");
            redirectAttributes.addFlashAttribute("tipoAlerta", "success");

            return "redirect:/factura/ver/" + idFactura;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoAlerta", "error");
            return "redirect:/productos/listar";
        }
    }

}

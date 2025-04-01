package com.cenfotec.soundwavemusic.controllers;
import com.cenfotec.soundwavemusic.models.Producto;
import com.cenfotec.soundwavemusic.services.CategoriaService;
import com.cenfotec.soundwavemusic.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/registrar")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.obtenerTodas());
        return "producto";
    }

    @GetMapping("/listar")
    public String listarProductos(Model model) {
        List<Producto> productos = productoService.getAllProductos();
        model.addAttribute("productos", productos);
        return "listarProductos"; // T Thymeleaf template
    }


    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto, RedirectAttributes redirectAttributes) {
        try {
            productoService.saveProducto(producto); // Guarda el producto
            int idProducto = producto.getId(); // Obtiene el ID generado

            redirectAttributes.addFlashAttribute("mensaje", "Producto guardado con éxito. Ahora registre el inventario.");
            redirectAttributes.addFlashAttribute("tipoAlerta", "success");

            return "redirect:/inventario/registrar?idProducto=" + idProducto;

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al guardar el producto.");
            redirectAttributes.addFlashAttribute("tipoAlerta", "error");
            return "redirect:/productos/registrar";
        }
    }
}

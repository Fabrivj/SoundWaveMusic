package com.cenfotec.soundwavemusic.controllers;

import com.cenfotec.soundwavemusic.models.Producto;
import com.cenfotec.soundwavemusic.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/registrar")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("producto", new Producto());
        return "producto";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto, RedirectAttributes redirectAttributes) {
        try {
            productoService.saveProducto(producto);
            redirectAttributes.addFlashAttribute("mensaje", "Producto guardado con éxito.");
            redirectAttributes.addFlashAttribute("tipoAlerta", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al guardar el producto.");
            redirectAttributes.addFlashAttribute("tipoAlerta", "error");
        }
        return "redirect:/productos/registrar";
    }
}

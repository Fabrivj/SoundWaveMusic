package com.cenfotec.soundwavemusic.controllers;


import com.cenfotec.soundwavemusic.models.Inventario;
import com.cenfotec.soundwavemusic.models.Producto;
import com.cenfotec.soundwavemusic.services.InventarioService;
import com.cenfotec.soundwavemusic.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private ProductoService productoService;

    // Mostrar formulario para registrar inventario
    @GetMapping("/registrar")
    public String mostrarFormularioInventario(@RequestParam("idProducto") int idProducto, Model model) {
        Producto producto = productoService.findById(idProducto);
        Inventario inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setEstado(true);
        model.addAttribute("inventario", inventario);
        return "inventario_form";
    }

    // Guardar inventario desde el formulario
    @PostMapping("/guardar")
    public String guardarInventario(@ModelAttribute Inventario inventario, RedirectAttributes redirectAttributes) {
        try {
            inventarioService.guardarOActualizar(inventario);
            redirectAttributes.addFlashAttribute("mensaje", "Inventario guardado con éxito.");
            redirectAttributes.addFlashAttribute("tipoAlerta", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al guardar el inventario.");
            redirectAttributes.addFlashAttribute("tipoAlerta", "error");
        }
        return "redirect:/productos/listar";
    }

    // Mostrar lista de inventario en vista
    @GetMapping("/listar")
    public String listarInventario(Model model) {
        model.addAttribute("inventarios", inventarioService.listarTodo());
        return "inventario_lista";
    }

    // Formulario para actualizar stock
    @GetMapping("/actualizar/{idProducto}")
    public String mostrarFormularioActualizar(@PathVariable int idProducto, Model model) {
        Inventario inventario = inventarioService.obtenerPorProductoId(idProducto)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));
        model.addAttribute("inventario", inventario);
        return "inventario_actualizar";
    }

    // Procesar actualización de stock
    @PostMapping("/actualizar")
    public String actualizarStock(@ModelAttribute Inventario inventario, RedirectAttributes redirectAttributes) {
        try {
            inventarioService.guardarOActualizar(inventario);
            redirectAttributes.addFlashAttribute("mensaje", "Inventario actualizado correctamente.");
            redirectAttributes.addFlashAttribute("tipoAlerta", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar el inventario.");
            redirectAttributes.addFlashAttribute("tipoAlerta", "error");
        }
        return "redirect:/inventario/listar";
    }
}
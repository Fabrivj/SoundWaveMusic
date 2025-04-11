package com.cenfotec.soundwavemusic.controllers;

import com.cenfotec.soundwavemusic.models.Producto;
import com.cenfotec.soundwavemusic.models.Usuario;
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
    public String listarProductos(Model model, @SessionAttribute(name = "usuario", required = false) Usuario usuario) {
        if (usuario == null) {
            return "redirect:/usuarios/login";
        }

        model.addAttribute("usuario", usuario); // 👈 ADD THIS LINE
        model.addAttribute("rol", usuario.getRol());
        List<Producto> productos = productoService.getProductosDisponibles();
        model.addAttribute("productos", productos);
        return "listarProductos";
    }



    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") int id, Model model) {
        Producto producto = productoService.findById(id);  // Fetch the product by its ID
        model.addAttribute("producto", producto);  // Pass the product to the view
        model.addAttribute("categorias", categoriaService.obtenerTodas());  // Pass the categories
        return "editarProducto"; // This should match the name of the HTML template (editarProducto.html)
    }

    @PostMapping("/actualizar")
    public String actualizarProducto(@ModelAttribute Producto producto, RedirectAttributes redirectAttributes) {
        try {
            productoService.saveProducto(producto);  // Save the updated product
            redirectAttributes.addFlashAttribute("mensaje", "Producto actualizado con éxito.");
            redirectAttributes.addFlashAttribute("tipoAlerta", "success");
            return "redirect:/productos/listar"; // Redirect to the product listing page
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar el producto.");
            redirectAttributes.addFlashAttribute("tipoAlerta", "error");
            return "redirect:/productos/editar/" + producto.getId(); // In case of error, go back to the edit page
        }
    }

    @PostMapping("/eliminar")
    public String eliminarProducto(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            productoService.deleteProducto(id);  // Call service method to delete the product
            redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado con éxito.");
            redirectAttributes.addFlashAttribute("tipoAlerta", "success");
            return "redirect:/productos/listar"; // Redirect to the product listing page after deletion
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar el producto.");
            redirectAttributes.addFlashAttribute("tipoAlerta", "error");
            return "redirect:/productos/listar"; // Redirect back to the listing page if error occurs
        }
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto, RedirectAttributes redirectAttributes) {
        try {
            Producto productoGuardado = productoService.saveProducto(producto);
            int idProducto = productoGuardado.getId(); // ahora sí tenés el ID generado

            redirectAttributes.addFlashAttribute("mensaje", "Producto guardado con éxito.");
            redirectAttributes.addFlashAttribute("tipoAlerta", "success");

            return "redirect:/inventario/registrar?idProducto=" + idProducto;

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al guardar el producto.");
            redirectAttributes.addFlashAttribute("tipoAlerta", "error");
            return "redirect:/productos/registrar";
        }
    }
}

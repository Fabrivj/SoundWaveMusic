package com.cenfotec.soundwavemusic.controllers;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cenfotec.soundwavemusic.models.Usuario;
import com.cenfotec.soundwavemusic.services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        boolean exito = usuarioService.registrarUsuario(usuario);

        if (exito) {
            // Successful registration
            redirectAttributes.addFlashAttribute("mensaje", "Usuario registrado con éxito.");
            redirectAttributes.addFlashAttribute("tipoAlerta", "success");

            // Redirect to the login page
            return "redirect:/usuarios/login";
        } else {
            // Failed registration
            redirectAttributes.addFlashAttribute("mensaje", "Hubo un error al registrar el usuario. Inténtalo nuevamente.");
            redirectAttributes.addFlashAttribute("tipoAlerta", "error");

            // Stay on the registration page
            return "redirect:/usuarios/registro";
        }
    }

    @GetMapping("/login")
    public String mostrarFormularioLogin() {
        return "login";
    }

    // Procesar el login
    @PostMapping("/login")
    public String autenticarUsuario(@RequestParam String correo, @RequestParam String contrasena,
                                    RedirectAttributes redirectAttributes, HttpSession session) {

        Usuario usuario = usuarioService.autenticarUsuario(correo, contrasena);

        if (usuario != null) {
            session.setAttribute("usuario", usuario); // ✅ Store user in session
            redirectAttributes.addFlashAttribute("mensaje", "¡Bienvenido!");
            return "redirect:/productos/listar";
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Credenciales incorrectas.");
            redirectAttributes.addFlashAttribute("tipoAlerta", "error");
            return "redirect:/usuarios/login";
        }
    }




    @GetMapping("/editar")
    public String mostrarFormularioEdicion() {
        return "editarUsuario";
    }

    @PostMapping("/buscar")
    public String buscarUsuario(@RequestParam String correo, Model model) {
        Usuario usuario = usuarioService.obtenerPorCorreo(correo);
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
        } else {
            model.addAttribute("mensaje", "Usuario no encontrado.");
            model.addAttribute("tipoAlerta", "error");
        }
        return "editarUsuario";
    }

    @PostMapping("/actualizar")
    public String actualizarUsuario(@ModelAttribute Usuario usuario, Model model) {
        boolean actualizado = usuarioService.actualizarUsuario(usuario);
        if (actualizado) {
            model.addAttribute("mensaje", "Usuario actualizado correctamente.");
            model.addAttribute("tipoAlerta", "success");
        } else {
            model.addAttribute("mensaje", "Error al actualizar usuario.");
            model.addAttribute("tipoAlerta", "error");
        }
        return "editarUsuario";
    }

    @PostMapping("/eliminar")
    public String eliminarUsuario(@RequestParam("idUsuario") long idUsuario, Model model) {
        boolean eliminado = usuarioService.eliminarUsuario(idUsuario);
        if (eliminado) {
            model.addAttribute("mensaje", "Usuario eliminado correctamente.");
            model.addAttribute("tipoAlerta", "success");
        } else {
            model.addAttribute("mensaje", "Error al eliminar usuario.");
            model.addAttribute("tipoAlerta", "error");
        }
        return "redirect:/usuarios/editar";
    }
}


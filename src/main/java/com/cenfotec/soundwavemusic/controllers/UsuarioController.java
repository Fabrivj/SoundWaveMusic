package com.cenfotec.soundwavemusic.controllers;

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
    public String registrarUsuario(@ModelAttribute Usuario usuario, Model model) {
        boolean exito = usuarioService.registrarUsuario(usuario);

        if (exito) {
            model.addAttribute("mensaje", "Usuario registrado con éxito.");
            model.addAttribute("tipoAlerta", "success");
        } else {
            model.addAttribute("mensaje", "Hubo un error al registrar el usuario. Inténtalo nuevamente.");
            model.addAttribute("tipoAlerta", "error");
        }
        return "registro";
    }

    @GetMapping("/login")
    public String mostrarFormularioLogin() {
        return "login";
    }

    // Procesar el login
    @PostMapping("/login")
    public String autenticarUsuario(@RequestParam String correo, @RequestParam String contrasena, Model model) {
        Usuario usuario = usuarioService.autenticarUsuario(correo, contrasena);

        if (usuario != null) {
            model.addAttribute("mensaje", "¡Bienvenido, " + usuario.getNombreUsuario() + "!");
            model.addAttribute("tipoAlerta", "success");

            return "catalogo";
        } else {
            model.addAttribute("mensaje", "Credenciales incorrectas.");
            model.addAttribute("tipoAlerta", "error");
            return "login";
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


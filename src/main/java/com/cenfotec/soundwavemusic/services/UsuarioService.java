package com.cenfotec.soundwavemusic.services;
import com.cenfotec.soundwavemusic.models.Usuario;
import com.cenfotec.soundwavemusic.repos.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario obtenerPorId(long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public boolean registrarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            return false;
        }

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        if (usuarioGuardado != null && usuarioGuardado.getId() != null) {

            usuarioRepository.findAll().forEach(System.out::println);
            return true;
        } else {
            return false;
        }
    }

    public Usuario autenticarUsuario(String correo, String password) {
        Usuario usuario = usuarioRepository.findByCorreo(correo);

        if (usuario != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            if (passwordEncoder.matches(password, new String(usuario.getPasswordBytes()))) {
                return usuario;
            }
        }

        return null;
    }

    public Usuario obtenerPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    public boolean actualizarUsuario(Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(usuario.getId());
        if (usuarioExistente.isPresent()) {
            Usuario usuarioActualizado = usuarioExistente.get();
            usuarioActualizado.setNombreUsuario(usuario.getNombreUsuario());
            usuarioActualizado.setCorreo(usuario.getCorreo());
            usuarioActualizado.setRol(usuario.getRol());
            usuarioRepository.save(usuarioActualizado);
            return true;
        }
        return false;
    }

    public boolean eliminarUsuario(long idUsuario) {
        if (usuarioRepository.existsById(idUsuario)) {
            usuarioRepository.deleteById(idUsuario);
            return true;
        }
        return false;
    }
}

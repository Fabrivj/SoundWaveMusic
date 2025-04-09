package com.cenfotec.soundwavemusic.models;

import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nombre_usuario", nullable = false)
    private String nombreUsuario;

    @Column(name = "correo", unique = true, nullable = false)
    private String correo;

    @Column(name = "rol", nullable = false)
    private String rol = "USER"; // Valor por defecto

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now(); // Valor por defecto

    @Transient  // No se almacena en la BD
    private String contrasena;

    @Lob
    @Column(name = "contrasena", nullable = false)
    private byte[] contrasenaEncriptada;

    public Usuario(String nombreUsuario, String correo, String contrasena) {
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.rol = "USER";
        this.fechaRegistro = LocalDateTime.now();
        this.contrasena = contrasena;
    }

    public Usuario() {}

    // Encripta la contraseña antes de guardarla en la base de datos
    @PrePersist
    @PreUpdate
    private void encriptarContrasena() {
        if (contrasena != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            this.contrasenaEncriptada = passwordEncoder.encode(contrasena).getBytes();
        }
    }

    // Método para validar la contraseña ingresada
    public boolean validarContrasena(String contrasenaIngresada) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(contrasenaIngresada, new String(contrasenaEncriptada));
    }

    // Este es el método que debes agregar para obtener la contraseña en formato byte[].
    public byte[] getPasswordBytes() {
        return contrasenaEncriptada;
    }
    // Getters y Setters
    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public byte[] getContrasenaEncriptada() {
        return contrasenaEncriptada;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
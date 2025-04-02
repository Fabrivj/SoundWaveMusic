package com.cenfotec.soundwavemusic.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_pedido")
    private LocalDateTime fechaPedido;

    @Column(name = "estado")
    private boolean estado;

    @Column(name = "direccion_envio")
    private String direccionEnvio;

    @Column(name = "total")
    private double total;

    // Getters y Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public String getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}

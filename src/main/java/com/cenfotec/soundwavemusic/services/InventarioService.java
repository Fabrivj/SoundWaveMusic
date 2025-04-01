package com.cenfotec.soundwavemusic.services;

import com.cenfotec.soundwavemusic.models.Inventario;
import com.cenfotec.soundwavemusic.repos.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    public List<Inventario> listarTodo() {
        return inventarioRepository.findAll();
    }

    public Optional<Inventario> obtenerPorProductoId(int idProducto) {
        return inventarioRepository.findByProductoId(idProducto);
    }

    public Inventario guardarOActualizar(Inventario inventario) {
        inventario.setUltimaActualizacion(LocalDateTime.now());
        return inventarioRepository.save(inventario);
    }

    public void actualizarStock(int idProducto, int cantidad) {
        Inventario inventario = inventarioRepository.findByProductoId(idProducto)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));
        inventario.setCantidadDisponible(inventario.getCantidadDisponible() + cantidad);
        inventario.setUltimaActualizacion(LocalDateTime.now());
        inventarioRepository.save(inventario);
    }
}

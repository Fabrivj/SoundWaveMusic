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
        if (inventario.getId() != 0) {
            // Ya existe, actualizar directamente
            Inventario existente = inventarioRepository.findById(inventario.getId())
                    .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));
            existente.setCantidadDisponible(inventario.getCantidadDisponible());
            existente.setEstado(inventario.isEstado());
            existente.setUltimaActualizacion(LocalDateTime.now());
            return inventarioRepository.save(existente);
        } else {
            // Buscar por producto, si ya tiene inventario
            Optional<Inventario> existente = inventarioRepository.findByProductoId(inventario.getProducto().getId());

            if (existente.isPresent()) {
                Inventario actualizado = existente.get();
                actualizado.setCantidadDisponible(inventario.getCantidadDisponible());
                actualizado.setEstado(inventario.isEstado());
                actualizado.setUltimaActualizacion(LocalDateTime.now());
                return inventarioRepository.save(actualizado);
            }

            // Si no existe, es nuevo
            inventario.setUltimaActualizacion(LocalDateTime.now());
            return inventarioRepository.save(inventario);
        }
    }

    public void actualizarStock(int idProducto, int cantidad) {
        Inventario inventario = inventarioRepository.findByProductoId(idProducto)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));
        inventario.setCantidadDisponible(inventario.getCantidadDisponible() + cantidad);
        inventario.setUltimaActualizacion(LocalDateTime.now());
        inventarioRepository.save(inventario);
    }
}

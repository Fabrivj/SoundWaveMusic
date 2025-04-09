package com.cenfotec.soundwavemusic.services;

import com.cenfotec.soundwavemusic.models.Inventario;
import com.cenfotec.soundwavemusic.models.Producto;
import com.cenfotec.soundwavemusic.repos.InventarioRepository;
import com.cenfotec.soundwavemusic.repos.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private InventarioRepository inventarioRepository;

    public List<Producto> getProductosDisponibles() {
        return productoRepository.findAll()
                .stream()
                .filter(p -> {
                    Optional<Inventario> inv = inventarioRepository.findByProductoId(p.getId());
                    return inv.isPresent() && inv.get().isEstado();
                })
                .collect(Collectors.toList());
    }


    public Producto saveProducto(Producto producto) {
        return productoRepository.save(producto);
    }


    public Producto findById(int id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public void deleteProducto(Long id) {
        int idProducto = Math.toIntExact(id);

        Inventario inventario = inventarioRepository.findByProductoId(idProducto)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));

        inventario.setEstado(false); //Soft delete
        inventario.setUltimaActualizacion(LocalDateTime.now());

        inventarioRepository.save(inventario);
    }

}

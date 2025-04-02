package com.cenfotec.soundwavemusic.services;

import com.cenfotec.soundwavemusic.models.Producto;
import com.cenfotec.soundwavemusic.repos.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    public void saveProducto(Producto producto) {
        productoRepository.save(producto);
    }


    public Producto findById(int id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public void deleteProducto(Long id) {
        // Find the product by id and delete it
        Producto producto = productoRepository.findById(Math.toIntExact(id)).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        productoRepository.delete(producto);
    }

}

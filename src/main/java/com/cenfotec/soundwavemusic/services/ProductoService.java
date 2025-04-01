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
}

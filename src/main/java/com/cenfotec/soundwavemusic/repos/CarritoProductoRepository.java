package com.cenfotec.soundwavemusic.repos;

import com.cenfotec.soundwavemusic.models.CarritoProducto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarritoProductoRepository extends JpaRepository<CarritoProducto, Integer> {
    List<CarritoProducto> findByCarritoId(int carritoId);
}

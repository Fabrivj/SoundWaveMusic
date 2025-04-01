package com.cenfotec.soundwavemusic.repos;

import com.cenfotec.soundwavemusic.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}


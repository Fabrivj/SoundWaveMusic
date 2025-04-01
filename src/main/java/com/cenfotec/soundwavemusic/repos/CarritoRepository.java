package com.cenfotec.soundwavemusic.repos;

import com.cenfotec.soundwavemusic.models.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
    Optional<Carrito> findByUsuarioIdAndEstadoTrue(long  usuarioId);
}

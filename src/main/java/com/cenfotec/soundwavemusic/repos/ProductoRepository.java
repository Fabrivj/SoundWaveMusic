package com.cenfotec.soundwavemusic.repos;

import com.cenfotec.soundwavemusic.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    @Query("""
    SELECT p FROM Producto p 
    JOIN Inventario i ON i.producto.id = p.id 
    WHERE i.estado = true 
    AND (
        LOWER(p.nombre_producto) LIKE LOWER(CONCAT('%', :query, '%')) 
        OR LOWER(p.categoria.nombre) LIKE LOWER(CONCAT('%', :query, '%'))
    )
""")
    List<Producto> buscarPorNombreOCategoria(@Param("query") String query);

}

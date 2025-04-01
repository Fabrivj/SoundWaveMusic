package com.cenfotec.soundwavemusic.repos;

import com.cenfotec.soundwavemusic.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}

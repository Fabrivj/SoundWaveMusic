package com.cenfotec.soundwavemusic.services;

import com.cenfotec.soundwavemusic.models.Categoria;
import com.cenfotec.soundwavemusic.repos.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> obtenerTodas() {
        return categoriaRepository.findAll();
    }
}


package com.cenfotec.soundwavemusic.repos;

import com.cenfotec.soundwavemusic.models.PedidoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoDetalleRepository extends JpaRepository<PedidoDetalle, Integer> {

    List<PedidoDetalle> findByPedidoId(int idPedido);
}

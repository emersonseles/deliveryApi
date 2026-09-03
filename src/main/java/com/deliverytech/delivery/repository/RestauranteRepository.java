package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    // Buscar e definir por categoria
    List<Restaurante> findByCategoria(String categoria);

    // Buscar restaurantes com taxa de entrega menor ou igual a um valor específico
    List<Restaurante> findByTaxaEntregaLessThanEqual(BigDecimal taxaEntrega);
}
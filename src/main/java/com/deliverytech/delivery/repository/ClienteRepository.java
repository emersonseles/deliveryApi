package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // buscar cliente por email
    Optional<Cliente> findByEmail(String email);

    // verificar se já existe cliente cadastrado com esse email
    boolean existsByEmail(String email);

    // verificar se o cliente está ativo
    boolean existsByEmailAndAtivoTrue(String email);

    // buscar clientes por nome
    List<Cliente> findByNomeContainingIgnoreCase(String nome);

    List<Cliente> findByTelefone(String telefone);

    List<Cliente> findByEnderecoContainingIgnoreCase(String endereco);

    @Query("SELECT c FROM Cliente c JOIN c.pedidos p WHERE p.statusPedido = :status")
    List<Cliente> findClientesComPedidosAtivos(@Param("status") String status);

    @Query("SELECT c FROM Cliente c WHERE c.endereco LIKE CONCAT('%', :endereco, '%')")
    List<Cliente> findClientesPorEndereco(@Param("endereco") String endereco);

    @Query("SELECT COUNT(c) FROM Cliente c")
    long countClientes();
}
package com.deliverytech.delivery.service;

import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.ItemPedido;
import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.enums.StatusPedido;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.PedidoRepository;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public Pedido criarPedido(Long clienteId, Long restauranteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado com o ID: " + clienteId));

        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado com o ID: " + restauranteId));

        Pedido pedido = new Pedido();
        pedido.setNumeroPedido("PED" + System.currentTimeMillis());
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatusPedido(StatusPedido.PENDENTE);
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setSubtotal(BigDecimal.ZERO);
        pedido.setTaxaEntrega(restaurante.getTaxaEntrega());
        pedido.setValorTotal(restaurante.getTaxaEntrega());
        pedido.setItens(new ArrayList<>());

        return pedidoRepository.save(pedido);
    }

    public Pedido adicionarItem(Long pedidoId, Long produtoId, Integer quantidade) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado com o ID: " + pedidoId));

        if (pedido.getStatusPedido() != StatusPedido.PENDENTE) {
            throw new IllegalArgumentException("Só é possível adicionar itens a pedidos pendentes.");
        }

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com o ID: " + produtoId));

        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }

        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(produto.getPreco());
        item.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(quantidade)));

        pedido.getItens().add(item);

        recalcularTotais(pedido);

        return pedidoRepository.save(pedido);
    }

    public Pedido confirmarPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado com o ID: " + pedidoId));

        if (pedido.getStatusPedido() != StatusPedido.PENDENTE) {
            throw new IllegalArgumentException("Apenas pedidos pendentes podem ser confirmados.");
        }

        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            throw new IllegalArgumentException("Não é possível confirmar um pedido sem itens.");
        }

        pedido.setStatusPedido(StatusPedido.CONFIRMADO);
        return pedidoRepository.save(pedido);
    }

    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteIdOrderByDataPedidoDesc(clienteId);
    }

    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorNumero(String numeroPedido) {
        return pedidoRepository.findByNumeroPedido(numeroPedido);
    }

    public Pedido atualizarStatus(Long pedidoId, StatusPedido status) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado com o ID: " + pedidoId));
        pedido.setStatusPedido(status);
        return pedidoRepository.save(pedido);
    }

    public Pedido cancelarPedido(Long pedidoId, String motivo) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado com o ID: " + pedidoId));

        if (pedido.getStatusPedido() == StatusPedido.ENTREGUE) {
            throw new IllegalArgumentException("Não é possível cancelar um pedido já entregue.");
        }

        pedido.setStatusPedido(StatusPedido.CANCELADO);
        pedido.setMotivoCancelamento(motivo);

        return pedidoRepository.save(pedido);
    }

    private void recalcularTotais(Pedido pedido) {
        BigDecimal subtotal = pedido.getItens().stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setSubtotal(subtotal);
        pedido.setValorTotal(subtotal.add(pedido.getTaxaEntrega()));
    }
}
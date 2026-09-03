package com.deliverytech.delivery.service;

import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * Cadastrando novo cliente
     */
    public Cliente cadastrarCliente(Cliente cliente) {
        // validação de email única
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado." + cliente.getEmail());
        }
        // validação de negocio
        validarDadosCliente(cliente);

        return clienteRepository.save(cliente);
    }

    private void validarDadosCliente(Cliente cliente) {
        // TODO: implementar validações de negócio (aguardando próxima aula)
    }
}
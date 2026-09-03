package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.entity.Cliente;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ClienteController {

    // Simulando um banco de dados com uma lista de cliente
    // https://localhost:8080/cliente
    @GetMapping("/cliente")
    public Cliente getCliente() {
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        return cliente;
    }

    // Simulando um banco de dados com uma lista de clientes
    // https://localhost:8080/clientes
    @GetMapping("/clientes")
    public List<Cliente> getClientes() {
        List<Cliente> clientes = new ArrayList<>();

        Cliente c1 = new Cliente();
        c1.setNome("João Silva");
        clientes.add(c1);

        Cliente c2 = new Cliente();
        c2.setNome("Maria Souza");
        clientes.add(c2);

        Cliente c3 = new Cliente();
        c3.setNome("Pedro Oliveira");
        clientes.add(c3);

        return clientes;
    }

    // O valor na URL diretamente injetado no método
    // https://localhost:8080/cliente/João
    @GetMapping("/cliente/{nome}")
    public Cliente getClienteByPathVariable(@PathVariable String nome) {
        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        return cliente;
    }

    // build onde o valor é passado como query param
    // https://localhost:8080/cliente/query?nome=João
    @GetMapping("/cliente/query")
    public Cliente getClienteByQueryParam(@RequestParam String nome) {
        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        return cliente;
    }

}
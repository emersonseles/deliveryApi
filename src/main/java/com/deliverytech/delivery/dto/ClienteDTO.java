package com.deliverytech.delivery.dto;

import jakarta.validation.constraints.*;

public class ClienteDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Telefone deve conter apenas dígitos, com DDD + número")
    private String telefone;

    @NotBlank(message = "Endereço é obrigatório")
    @Size(max = 200, message = "Endereço deve conter no máximo 200 caracteres")
    private String endereco;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
}
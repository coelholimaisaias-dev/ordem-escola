package br.com.coelholimaisaias_dev.ordem_escola.web.dto;

import jakarta.validation.constraints.NotBlank;

public record EmpresaCreateRequest(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "CNPJ é obrigatório")
        String cnpj
) {}

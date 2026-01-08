package br.com.coelholimaisaias_dev.ordem_escola.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmpresaUpdateRequest(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "CNPJ é obrigatório")
        String cnpj,

        @NotNull(message = "Status é obrigatório")
        Boolean ativo
) {}

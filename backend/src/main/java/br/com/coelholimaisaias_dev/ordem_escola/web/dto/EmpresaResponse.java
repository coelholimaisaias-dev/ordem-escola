package br.com.coelholimaisaias_dev.ordem_escola.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EmpresaResponse(
        Long id,
        String nome,
        String cnpj,
        Boolean ativo
) {}

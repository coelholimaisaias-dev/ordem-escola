package br.com.coelholimaisaias_dev.ordem_escola.web.dto;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Turno;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record TurmaUpdateRequest(
        @NotNull(message = "Empresa é obrigatória")
        Long empresaId,

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotNull(message = "Turno é obrigatório")
        Turno turno,

        @Positive(message = "Valor base deve ser positivo")
        BigDecimal valorBase,

        @Positive(message = "Capacidade deve ser positiva")
        Integer capacidade,

        @NotNull(message = "Status é obrigatório")
        Boolean ativo
) {}

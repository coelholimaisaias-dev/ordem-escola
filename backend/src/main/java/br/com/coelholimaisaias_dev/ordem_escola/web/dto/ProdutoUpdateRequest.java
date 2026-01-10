package br.com.coelholimaisaias_dev.ordem_escola.web.dto;

import br.com.coelholimaisaias_dev.ordem_escola.domain.TipoProduto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProdutoUpdateRequest(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        String descricao,

        @NotNull(message = "Tipo é obrigatório")
        TipoProduto tipo,

        @NotNull(message = "Valor unitário é obrigatório")
        @Positive(message = "Valor unitário deve ser positivo")
        BigDecimal valorUnitario,

        @NotNull(message = "Status é obrigatório")
        Boolean ativo
) {}

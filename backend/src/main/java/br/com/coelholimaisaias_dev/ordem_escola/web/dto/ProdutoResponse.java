package br.com.coelholimaisaias_dev.ordem_escola.web.dto;

import br.com.coelholimaisaias_dev.ordem_escola.domain.TipoProduto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProdutoResponse(
        Long id,
        Long empresaId,
        String empresaNome,
        String nome,
        String descricao,
        TipoProduto tipo,
        BigDecimal valorUnitario,
        Boolean ativo
) {}

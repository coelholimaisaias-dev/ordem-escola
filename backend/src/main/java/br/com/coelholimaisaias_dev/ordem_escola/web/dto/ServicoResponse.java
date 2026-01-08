package br.com.coelholimaisaias_dev.ordem_escola.web.dto;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Turno;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ServicoResponse(
        Long id,
        Long empresaId,
        String empresaNome,
        String nome,
        Turno turno,
        BigDecimal valorBase,
        Boolean ativo
) {}

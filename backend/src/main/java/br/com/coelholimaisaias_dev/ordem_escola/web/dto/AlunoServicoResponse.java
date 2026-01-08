package br.com.coelholimaisaias_dev.ordem_escola.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AlunoServicoResponse(
        Long id,
        Long alunoId,
        String alunoNome,
        Long servicoId,
        String servicoNome,
        Instant dataInicio,
        Boolean ativo
) {}

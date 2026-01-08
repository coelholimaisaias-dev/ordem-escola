package br.com.coelholimaisaias_dev.ordem_escola.web.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record AlunoServicoCreateRequest(
        @NotNull(message = "Aluno é obrigatório")
        Long alunoId,

        @NotNull(message = "Serviço é obrigatório")
        Long servicoId,

        Instant dataInicio
) {}

package br.com.coelholimaisaias_dev.ordem_escola.web.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record MatriculaUpdateRequest(
        @NotNull(message = "Aluno é obrigatório")
        Long alunoId,

        @NotNull(message = "Turma é obrigatória")
        Long turmaId,

        Instant dataMatricula,

        @NotNull(message = "Status é obrigatório")
        Boolean ativo,

        Instant dataCancelamento
) {}

package br.com.coelholimaisaias_dev.ordem_escola.web.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record MatriculaCreateRequest(
        @NotNull(message = "Aluno é obrigatório")
        Long alunoId,

        @NotNull(message = "Turma é obrigatória")
        Long turmaId,

        Instant dataMatricula
) {}

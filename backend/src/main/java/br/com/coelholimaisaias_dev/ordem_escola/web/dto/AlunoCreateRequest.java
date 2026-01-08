package br.com.coelholimaisaias_dev.ordem_escola.web.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AlunoCreateRequest(
        @NotNull(message = "Usuário é obrigatório")
        Long usuarioId,

        LocalDate dataNascimento
) {}

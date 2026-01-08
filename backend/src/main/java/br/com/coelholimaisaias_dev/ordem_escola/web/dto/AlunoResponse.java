package br.com.coelholimaisaias_dev.ordem_escola.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AlunoResponse(
        Long id,
        Long usuarioId,
        String usuarioNome,
        String usuarioEmail,
        LocalDate dataNascimento,
        Boolean ativo
) {}

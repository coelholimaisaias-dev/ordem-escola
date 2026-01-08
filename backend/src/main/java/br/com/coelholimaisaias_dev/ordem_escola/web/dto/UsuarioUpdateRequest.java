package br.com.coelholimaisaias_dev.ordem_escola.web.dto;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioUpdateRequest(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @Email(message = "Email inválido")
        @NotBlank(message = "Email é obrigatório")
        String email,

        @NotNull(message = "Empresa é obrigatória")
        Long empresaId,

        @NotNull(message = "Perfil é obrigatório")
        Perfil perfil,

        @NotNull(message = "Status é obrigatório")
        Boolean ativo
) {}

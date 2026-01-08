package br.com.coelholimaisaias_dev.ordem_escola.web.dto;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Perfil;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        Long empresaId,
        String empresaNome,
        Perfil perfil,
        Boolean ativo
) {}

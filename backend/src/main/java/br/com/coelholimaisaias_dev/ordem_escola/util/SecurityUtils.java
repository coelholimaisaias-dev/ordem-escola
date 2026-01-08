package br.com.coelholimaisaias_dev.ordem_escola.util;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Usuario;
import br.com.coelholimaisaias_dev.ordem_escola.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityUtils {

    private final UsuarioRepository usuarioRepository;

    public SecurityUtils(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Obtém o email do usuário autenticado
     */
    public Optional<String> getAuthenticatedUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return Optional.of(auth.getName());
        }
        return Optional.empty();
    }

    /**
     * Obtém a entidade Usuario do usuário autenticado
     */
    public Optional<Usuario> getAuthenticatedUser() {
        return getAuthenticatedUserEmail()
                .flatMap(usuarioRepository::findByEmail);
    }

    /**
     * Obtém o empresaId do usuário autenticado
     */
    public Optional<Long> getAuthenticatedUserEmpresaId() {
        return getAuthenticatedUser()
                .flatMap(user -> Optional.ofNullable(user.getEmpresa()))
                .map(empresa -> empresa.getId());
    }

    /**
     * Verifica se o usuário autenticado é ADMIN
     */
    public boolean isAdmin() {
        return getAuthenticatedUser()
                .map(usuario -> "ADMIN".equals(usuario.getPerfil().toString()))
                .orElse(false);
    }

    /**
     * Verifica se o usuário autenticado é CLIENTE
     */
    public boolean isCliente() {
        return getAuthenticatedUser()
                .map(usuario -> "CLIENTE".equals(usuario.getPerfil().toString()))
                .orElse(false);
    }
}

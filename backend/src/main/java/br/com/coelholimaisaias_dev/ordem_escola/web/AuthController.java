package br.com.coelholimaisaias_dev.ordem_escola.web;

import br.com.coelholimaisaias_dev.ordem_escola.repository.UsuarioRepository;
import br.com.coelholimaisaias_dev.ordem_escola.security.JwtUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping({"/auth", "/api/auth"})
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
    }

    // DTO para login com validação
    public record LoginRequest(
            @Email String email,
            @NotBlank String senha
    ) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.senha())
        );

        String token = jwtUtil.generateToken(auth.getName());
        
        // Buscar o usuário para retornar nome completo e perfil
        var usuario = usuarioRepository.findByEmail(req.email())
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado após autenticação bem-sucedida"));
        
        if (usuario.getPerfil() == null) {
            throw new IllegalStateException("Usuário sem perfil definido");
        }

        return ResponseEntity.ok(Map.of(
                "token", token,
                "username", auth.getName(),
                "name", usuario.getNome(),
                "perfil", usuario.getPerfil().toString()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // JWT stateless logout: client discards token. No server invalidation here.
        return ResponseEntity.ok(Map.of("message", "logged out"));
    }
}

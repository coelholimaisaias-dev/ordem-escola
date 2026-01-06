package br.com.coelholimaisaias_dev.ordem_escola.web;

import br.com.coelholimaisaias_dev.ordem_escola.security.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {

    // ✅ Retorna o usuário logado
    @GetMapping("/me")
    public Map<String, String> me() {
        return Map.of(
                "username", SecurityUtils.getCurrentUserEmail()
        );
    }

    // ✅ Endpoint apenas para ADMIN
    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> adminTest() {
        return Map.of(
                "message", "Acesso permitido apenas para ADMIN",
                "username", SecurityUtils.getCurrentUserEmail()
        );
    }

    // ✅ Endpoint para ADMIN e CLIENTE
    @GetMapping("/cliente/test")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public Map<String, String> clienteTest() {
        return Map.of(
                "message", "Acesso permitido para ADMIN ou CLIENTE",
                "username", SecurityUtils.getCurrentUserEmail()
        );
    }
}

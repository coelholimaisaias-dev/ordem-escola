package br.com.coelholimaisaias_dev.ordem_escola.web;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Usuario;
import br.com.coelholimaisaias_dev.ordem_escola.service.UsuarioService;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.UsuarioCreateRequest;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.UsuarioResponse;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.UsuarioUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public List<UsuarioResponse> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long empresaId,
            @RequestParam(required = false) String perfil,
            @RequestParam(required = false) Boolean ativo
    ) {
        return service.listarPorFiltro(nome, email, empresaId, perfil, ativo);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public UsuarioResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorIdResponse(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponse criar(@Valid @RequestBody UsuarioCreateRequest request) {
        return service.criar(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateRequest request
    ) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponse alterarStatus(
            @PathVariable Long id,
            @RequestBody @Valid UsuarioUpdateRequest request
    ) {
        return service.atualizar(id, request);
    }
}


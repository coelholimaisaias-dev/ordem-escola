package br.com.coelholimaisaias_dev.ordem_escola.web;

import br.com.coelholimaisaias_dev.ordem_escola.service.AlunoService;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.AlunoCreateRequest;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.AlunoResponse;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.AlunoUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService service;

    public AlunoController(AlunoService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public List<AlunoResponse> listar(
            @RequestParam(required = false) String usuarioNome,
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) Boolean ativo
    ) {
        return service.listarPorFiltro(usuarioNome, usuarioId, ativo);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public AlunoResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorIdResponse(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public AlunoResponse criar(@Valid @RequestBody AlunoCreateRequest request) {
        return service.criar(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public AlunoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AlunoUpdateRequest request
    ) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}


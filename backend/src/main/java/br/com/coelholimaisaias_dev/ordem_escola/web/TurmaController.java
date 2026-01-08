package br.com.coelholimaisaias_dev.ordem_escola.web;

import br.com.coelholimaisaias_dev.ordem_escola.service.TurmaService;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.TurmaCreateRequest;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.TurmaResponse;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.TurmaUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turmas")
public class TurmaController {

    private final TurmaService service;

    public TurmaController(TurmaService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public List<TurmaResponse> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Long empresaId,
            @RequestParam(required = false) String turno,
            @RequestParam(required = false) Integer capacidade,
            @RequestParam(required = false) Boolean ativo
    ) {
        return service.listarPorFiltro(nome, empresaId, turno, capacidade, ativo);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public TurmaResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorIdResponse(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public TurmaResponse criar(@Valid @RequestBody TurmaCreateRequest request) {
        return service.criar(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public TurmaResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TurmaUpdateRequest request
    ) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}

package br.com.coelholimaisaias_dev.ordem_escola.web;

import br.com.coelholimaisaias_dev.ordem_escola.service.AlunoServicoService;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.AlunoServicoCreateRequest;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.AlunoServicoResponse;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.AlunoServicoUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aluno-servicos")
public class AlunoServicoController {

    private final AlunoServicoService service;

    public AlunoServicoController(AlunoServicoService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public List<AlunoServicoResponse> listar(
            @RequestParam(required = false) Long alunoId,
            @RequestParam(required = false) Long servicoId,
            @RequestParam(required = false) Boolean ativo
    ) {
        return service.listarPorFiltro(alunoId, servicoId, ativo);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public AlunoServicoResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorIdResponse(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public AlunoServicoResponse criar(@Valid @RequestBody AlunoServicoCreateRequest request) {
        return service.criar(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public AlunoServicoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AlunoServicoUpdateRequest request
    ) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}


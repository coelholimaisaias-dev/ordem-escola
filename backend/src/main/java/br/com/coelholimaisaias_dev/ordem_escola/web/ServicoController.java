package br.com.coelholimaisaias_dev.ordem_escola.web;

import br.com.coelholimaisaias_dev.ordem_escola.service.ServicoService;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.ServicoCreateRequest;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.ServicoResponse;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.ServicoUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService service;

    public ServicoController(ServicoService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public List<ServicoResponse> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Long empresaId,
            @RequestParam(required = false) String turno,
            @RequestParam(required = false) Boolean ativo
    ) {
        return service.listarPorFiltro(nome, empresaId, turno, ativo);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ServicoResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorIdResponse(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ServicoResponse criar(@Valid @RequestBody ServicoCreateRequest request) {
        return service.criar(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ServicoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ServicoUpdateRequest request
    ) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}


package br.com.coelholimaisaias_dev.ordem_escola.web;

import br.com.coelholimaisaias_dev.ordem_escola.service.EmpresaService;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.EmpresaCreateRequest;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.EmpresaResponse;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.EmpresaUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    private final EmpresaService service;

    public EmpresaController(EmpresaService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public List<EmpresaResponse> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cnpj,
            @RequestParam(required = false) Boolean ativo
    ) {
        return service.listarPorFiltro(nome, cnpj, ativo);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public EmpresaResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorIdResponse(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public EmpresaResponse criar(@Valid @RequestBody EmpresaCreateRequest request) {
        return service.criar(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public EmpresaResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EmpresaUpdateRequest request
    ) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}


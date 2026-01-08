package br.com.coelholimaisaias_dev.ordem_escola.web;

import br.com.coelholimaisaias_dev.ordem_escola.service.TurmaService;
import br.com.coelholimaisaias_dev.ordem_escola.util.SecurityUtils;
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
    private final SecurityUtils securityUtils;

    public TurmaController(TurmaService service, SecurityUtils securityUtils) {
        this.service = service;
        this.securityUtils = securityUtils;
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
        // CLIENTE vê só turmas da sua empresa
        if (securityUtils.isCliente()) {
            empresaId = securityUtils.getAuthenticatedUserEmpresaId().orElse(null);
        }
        return service.listarPorFiltro(nome, empresaId, turno, capacidade, ativo);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public TurmaResponse buscarPorId(@PathVariable Long id) {
        TurmaResponse turma = service.buscarPorIdResponse(id);
        
        // CLIENTE só acessa turmas da sua empresa
        if (securityUtils.isCliente()) {
            Long empresaId = securityUtils.getAuthenticatedUserEmpresaId().orElse(null);
            if (!turma.empresaId().equals(empresaId)) {
                throw new IllegalStateException("Acesso negado a esta turma");
            }
        }
        
        return turma;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public TurmaResponse criar(@Valid @RequestBody TurmaCreateRequest request) {
        // CLIENTE sempre cria na sua empresa
        if (securityUtils.isCliente()) {
            Long empresaId = securityUtils.getAuthenticatedUserEmpresaId().orElse(null);
            var novoRequest = new TurmaCreateRequest(
                    empresaId,
                    request.nome(),
                    request.turno(),
                    request.valorBase(),
                    request.capacidade()
            );
            return service.criar(novoRequest);
        }
        return service.criar(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public TurmaResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TurmaUpdateRequest request
    ) {
        TurmaResponse turmaAnterior = service.buscarPorIdResponse(id);
        
        // CLIENTE só atualiza turmas da sua empresa
        if (securityUtils.isCliente()) {
            Long empresaId = securityUtils.getAuthenticatedUserEmpresaId().orElse(null);
            if (!turmaAnterior.empresaId().equals(empresaId)) {
                throw new IllegalStateException("Acesso negado a esta turma");
            }
            
            var novoRequest = new TurmaUpdateRequest(
                    empresaId,
                    request.nome(),
                    request.turno(),
                    request.valorBase(),
                    request.capacidade(),
                    request.ativo()
            );
            return service.atualizar(id, novoRequest);
        }
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public void deletar(@PathVariable Long id) {
        TurmaResponse turma = service.buscarPorIdResponse(id);
        
        // CLIENTE só deleta turmas da sua empresa
        if (securityUtils.isCliente()) {
            Long empresaId = securityUtils.getAuthenticatedUserEmpresaId().orElse(null);
            if (!turma.empresaId().equals(empresaId)) {
                throw new IllegalStateException("Acesso negado a esta turma");
            }
        }
        
        service.deletar(id);
    }
}

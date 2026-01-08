package br.com.coelholimaisaias_dev.ordem_escola.web;

import br.com.coelholimaisaias_dev.ordem_escola.service.AlunoService;
import br.com.coelholimaisaias_dev.ordem_escola.util.SecurityUtils;
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
    private final SecurityUtils securityUtils;

    public AlunoController(AlunoService service, SecurityUtils securityUtils) {
        this.service = service;
        this.securityUtils = securityUtils;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public List<AlunoResponse> listar(
            @RequestParam(required = false) String usuarioNome,
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) Boolean ativo
    ) {
        // CLIENTE vê só alunos da sua empresa (filtra via empresa do usuário)
        return service.listarPorFiltro(usuarioNome, usuarioId, ativo, 
                securityUtils.isAdmin() ? null : securityUtils.getAuthenticatedUserEmpresaId().orElse(null));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public AlunoResponse buscarPorId(@PathVariable Long id) {
        AlunoResponse aluno = service.buscarPorIdResponse(id);
        
        // CLIENTE só acessa alunos da sua empresa (via usuario)
        if (securityUtils.isCliente()) {
            Long empresaId = securityUtils.getAuthenticatedUserEmpresaId().orElse(null);
            if (!aluno.usuarioEmpresaId().equals(empresaId)) {
                throw new IllegalStateException("Acesso negado a este aluno");
            }
        }
        
        return aluno;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public AlunoResponse criar(@Valid @RequestBody AlunoCreateRequest request) {
        // CLIENTE só cria alunos de usuários da sua empresa
        if (securityUtils.isCliente()) {
            Long empresaId = securityUtils.getAuthenticatedUserEmpresaId().orElse(null);
            service.validarUsuarioPertenceAEmpresa(request.usuarioId(), empresaId);
        }
        return service.criar(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public AlunoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AlunoUpdateRequest request
    ) {
        AlunoResponse alunoAnterior = service.buscarPorIdResponse(id);
        
        // CLIENTE só atualiza alunos da sua empresa
        if (securityUtils.isCliente()) {
            Long empresaId = securityUtils.getAuthenticatedUserEmpresaId().orElse(null);
            if (!alunoAnterior.usuarioEmpresaId().equals(empresaId)) {
                throw new IllegalStateException("Acesso negado a este aluno");
            }
            service.validarUsuarioPertenceAEmpresa(request.usuarioId(), empresaId);
        }
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public void deletar(@PathVariable Long id) {
        AlunoResponse aluno = service.buscarPorIdResponse(id);
        
        // CLIENTE só deleta alunos da sua empresa
        if (securityUtils.isCliente()) {
            Long empresaId = securityUtils.getAuthenticatedUserEmpresaId().orElse(null);
            if (!aluno.usuarioEmpresaId().equals(empresaId)) {
                throw new IllegalStateException("Acesso negado a este aluno");
            }
        }
        
        service.deletar(id);
    }
}


package br.com.coelholimaisaias_dev.ordem_escola.web;

import br.com.coelholimaisaias_dev.ordem_escola.service.ProdutoService;
import br.com.coelholimaisaias_dev.ordem_escola.util.SecurityUtils;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.ProdutoCreateRequest;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.ProdutoResponse;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.ProdutoUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService service;
    private final SecurityUtils securityUtils;

    public ProdutoController(ProdutoService service, SecurityUtils securityUtils) {
        this.service = service;
        this.securityUtils = securityUtils;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public List<ProdutoResponse> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Boolean ativo
    ) {
        // CLIENTE só vê produtos da sua empresa
        Long empresaId = null;
        if (securityUtils.isCliente()) {
            empresaId = securityUtils.getAuthenticatedUserEmpresaId().orElse(null);
        }
        
        return service.listarPorFiltro(nome, tipo, ativo, empresaId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ProdutoResponse buscarPorId(@PathVariable Long id) {
        ProdutoResponse produto = service.buscarPorIdResponse(id);
        
        // CLIENTE só acessa produtos da sua empresa
        if (securityUtils.isCliente()) {
            Long empresaId = securityUtils.getAuthenticatedUserEmpresaId().orElse(null);
            if (!produto.empresaId().equals(empresaId)) {
                throw new IllegalStateException("Acesso negado a este produto");
            }
        }
        
        return produto;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ProdutoResponse criar(@Valid @RequestBody ProdutoCreateRequest request) {
        // CLIENTE só cria produtos na sua empresa
        if (securityUtils.isCliente()) {
            Long empresaId = securityUtils.getAuthenticatedUserEmpresaId().orElse(null);
            if (!request.empresaId().equals(empresaId)) {
                throw new IllegalStateException("Você só pode criar produtos para sua empresa");
            }
        }
        
        return service.criar(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ProdutoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoUpdateRequest request
    ) {
        ProdutoResponse produto = service.buscarPorIdResponse(id);
        
        // CLIENTE só atualiza produtos da sua empresa
        if (securityUtils.isCliente()) {
            Long empresaId = securityUtils.getAuthenticatedUserEmpresaId().orElse(null);
            if (!produto.empresaId().equals(empresaId)) {
                throw new IllegalStateException("Acesso negado a este produto");
            }
        }
        
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public void deletar(@PathVariable Long id) {
        ProdutoResponse produto = service.buscarPorIdResponse(id);
        
        // CLIENTE só deleta produtos da sua empresa
        if (securityUtils.isCliente()) {
            Long empresaId = securityUtils.getAuthenticatedUserEmpresaId().orElse(null);
            if (!produto.empresaId().equals(empresaId)) {
                throw new IllegalStateException("Acesso negado a este produto");
            }
        }
        
        service.deletar(id);
    }
}

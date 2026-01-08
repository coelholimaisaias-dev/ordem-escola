package br.com.coelholimaisaias_dev.ordem_escola.web;

import br.com.coelholimaisaias_dev.ordem_escola.service.ServicoService;
import br.com.coelholimaisaias_dev.ordem_escola.util.SecurityUtils;
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
    private final SecurityUtils securityUtils;

    public ServicoController(ServicoService service, SecurityUtils securityUtils) {
        this.service = service;
        this.securityUtils = securityUtils;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public List<ServicoResponse> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Long empresaId,
            @RequestParam(required = false) String turno,
            @RequestParam(required = false) Boolean ativo
    ) {
        // CLIENTE vê só serviços da sua empresa
        if (securityUtils.isCliente()) {
            empresaId = securityUtils.getAuthenticatedUserEmpresaId().orElse(null);
        }
        return service.listarPorFiltro(nome, empresaId, turno, ativo);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ServicoResponse buscarPorId(@PathVariable Long id) {
        ServicoResponse servico = service.buscarPorIdResponse(id);
        
        // CLIENTE só acessa serviços da sua empresa
        if (securityUtils.isCliente()) {
            Long empresaId = securityUtils.getAuthenticatedUserEmpresaId().orElse(null);
            if (!servico.empresaId().equals(empresaId)) {
                throw new IllegalStateException("Acesso negado a este serviço");
            }
        }
        
        return servico;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ServicoResponse criar(@Valid @RequestBody ServicoCreateRequest request) {
        // CLIENTE sempre cria na sua empresa
        if (securityUtils.isCliente()) {
            Long empresaId = securityUtils.getAuthenticatedUserEmpresaId().orElse(null);
            var novoRequest = new ServicoCreateRequest(
                    empresaId,
                    request.nome(),
                    request.turno(),
                    request.valorBase()
            );
            return service.criar(novoRequest);
        }
        return service.criar(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ServicoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ServicoUpdateRequest request
    ) {
        ServicoResponse servicoAnterior = service.buscarPorIdResponse(id);
        
        // CLIENTE só atualiza serviços da sua empresa
        if (securityUtils.isCliente()) {
            Long empresaId = securityUtils.getAuthenticatedUserEmpresaId().orElse(null);
            if (!servicoAnterior.empresaId().equals(empresaId)) {
                throw new IllegalStateException("Acesso negado a este serviço");
            }
            
            var novoRequest = new ServicoUpdateRequest(
                    empresaId,
                    request.nome(),
                    request.turno(),
                    request.valorBase(),
                    request.ativo()
            );
            return service.atualizar(id, novoRequest);
        }
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public void deletar(@PathVariable Long id) {
        ServicoResponse servico = service.buscarPorIdResponse(id);
        
        // CLIENTE só deleta serviços da sua empresa
        if (securityUtils.isCliente()) {
            Long empresaId = securityUtils.getAuthenticatedUserEmpresaId().orElse(null);
            if (!servico.empresaId().equals(empresaId)) {
                throw new IllegalStateException("Acesso negado a este serviço");
            }
        }
        
        service.deletar(id);
    }
}


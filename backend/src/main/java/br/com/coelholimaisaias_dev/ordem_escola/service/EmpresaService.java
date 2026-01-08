package br.com.coelholimaisaias_dev.ordem_escola.service;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Empresa;
import br.com.coelholimaisaias_dev.ordem_escola.repository.EmpresaRepository;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.EmpresaCreateRequest;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.EmpresaResponse;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.EmpresaUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpresaService extends BaseService<Empresa> {

    private final EmpresaRepository repository;

    public EmpresaService(EmpresaRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public List<EmpresaResponse> listarPorFiltro(String nome, String cnpj, Boolean ativo) {
        return repository.findAllByAtivoTrue()
                .stream()
                .filter(e -> nome == null || e.getNome().toLowerCase().contains(nome.toLowerCase()))
                .filter(e -> cnpj == null || e.getCnpj().contains(cnpj))
                .filter(e -> ativo == null || e.getAtivo().equals(ativo))
                .map(this::toResponse)
                .toList();
    }

    public List<EmpresaResponse> listarResponse() {
        return repository.findAllByAtivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public EmpresaResponse buscarPorIdResponse(Long id) {
        Empresa empresa = buscarPorId(id);
        return toResponse(empresa);
    }

    @Transactional
    public EmpresaResponse criar(EmpresaCreateRequest request) {
        var empresa = Empresa.builder()
                .nome(request.nome())
                .cnpj(request.cnpj())
                .ativo(true)
                .build();
        
        empresa = repository.save(empresa);
        return toResponse(empresa);
    }

    @Transactional
    public EmpresaResponse atualizar(Long id, EmpresaUpdateRequest request) {
        Empresa empresa = buscarPorId(id);
        
        empresa.setNome(request.nome());
        empresa.setCnpj(request.cnpj());
        empresa.setAtivo(request.ativo());
        
        empresa = repository.save(empresa);
        return toResponse(empresa);
    }

    private EmpresaResponse toResponse(Empresa empresa) {
        return new EmpresaResponse(
                empresa.getId(),
                empresa.getNome(),
                empresa.getCnpj(),
                empresa.getAtivo()
        );
    }
}

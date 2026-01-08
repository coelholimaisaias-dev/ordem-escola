package br.com.coelholimaisaias_dev.ordem_escola.service;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Servico;
import br.com.coelholimaisaias_dev.ordem_escola.repository.ServicoRepository;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.ServicoCreateRequest;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.ServicoResponse;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.ServicoUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServicoService extends BaseService<Servico> {

    private final ServicoRepository repository;
    private final EmpresaService empresaService;

    public ServicoService(ServicoRepository repository, EmpresaService empresaService) {
        super(repository);
        this.repository = repository;
        this.empresaService = empresaService;
    }

    public List<ServicoResponse> listarPorFiltro(String nome, Long empresaId, String turno, Boolean ativo) {
        return repository.findAllByAtivoTrue()
                .stream()
                .filter(s -> nome == null || s.getNome().toLowerCase().contains(nome.toLowerCase()))
                .filter(s -> empresaId == null || s.getEmpresa().getId().equals(empresaId))
                .filter(s -> turno == null || s.getTurno().toString().equals(turno))
                .filter(s -> ativo == null || s.getAtivo().equals(ativo))
                .map(this::toResponse)
                .toList();
    }

    public ServicoResponse buscarPorIdResponse(Long id) {
        Servico servico = buscarPorId(id);
        return toResponse(servico);
    }

    @Transactional
    public ServicoResponse criar(ServicoCreateRequest request) {
        var empresa = empresaService.buscarPorId(request.empresaId());
        
        var servico = Servico.builder()
                .empresa(empresa)
                .nome(request.nome())
                .turno(request.turno())
                .valorBase(request.valorBase())
                .ativo(true)
                .build();
        
        servico = repository.save(servico);
        return toResponse(servico);
    }

    @Transactional
    public ServicoResponse atualizar(Long id, ServicoUpdateRequest request) {
        Servico servico = buscarPorId(id);
        var empresa = empresaService.buscarPorId(request.empresaId());
        
        servico.setEmpresa(empresa);
        servico.setNome(request.nome());
        servico.setTurno(request.turno());
        servico.setValorBase(request.valorBase());
        servico.setAtivo(request.ativo());
        
        servico = repository.save(servico);
        return toResponse(servico);
    }

    private ServicoResponse toResponse(Servico servico) {
        return new ServicoResponse(
                servico.getId(),
                servico.getEmpresa() != null ? servico.getEmpresa().getId() : null,
                servico.getEmpresa() != null ? servico.getEmpresa().getNome() : null,
                servico.getNome(),
                servico.getTurno(),
                servico.getValorBase(),
                servico.getAtivo()
        );
    }
}


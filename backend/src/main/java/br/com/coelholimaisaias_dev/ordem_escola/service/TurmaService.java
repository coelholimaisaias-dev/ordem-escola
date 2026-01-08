package br.com.coelholimaisaias_dev.ordem_escola.service;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Turma;
import br.com.coelholimaisaias_dev.ordem_escola.repository.TurmaRepository;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.TurmaCreateRequest;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.TurmaResponse;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.TurmaUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TurmaService extends BaseService<Turma> {

    private final TurmaRepository repository;
    private final EmpresaService empresaService;

    public TurmaService(TurmaRepository repository, EmpresaService empresaService) {
        super(repository);
        this.repository = repository;
        this.empresaService = empresaService;
    }

    public List<TurmaResponse> listarResponse() {
        return repository.findAllByAtivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TurmaResponse> listarPorFiltro(String nome, Long empresaId, String turno, Integer capacidade, Boolean ativo) {
        return repository.findAllByAtivoTrue()
                .stream()
                .filter(t -> nome == null || t.getNome().toLowerCase().contains(nome.toLowerCase()))
                .filter(t -> empresaId == null || t.getEmpresa().getId().equals(empresaId))
                .filter(t -> turno == null || t.getTurno().toString().equals(turno))
                .filter(t -> capacidade == null || t.getCapacidade().equals(capacidade))
                .filter(t -> ativo == null || t.getAtivo().equals(ativo))
                .map(this::toResponse)
                .toList();
    }

    public TurmaResponse buscarPorIdResponse(Long id) {
        Turma turma = buscarPorId(id);
        return toResponse(turma);
    }

    @Transactional
    public TurmaResponse criar(TurmaCreateRequest request) {
        var empresa = empresaService.buscarPorId(request.empresaId());
        
        var turma = Turma.builder()
                .empresa(empresa)
                .nome(request.nome())
                .turno(request.turno())
                .valorBase(request.valorBase())
                .capacidade(request.capacidade())
                .ativo(true)
                .build();
        
        turma = repository.save(turma);
        return toResponse(turma);
    }

    @Transactional
    public TurmaResponse atualizar(Long id, TurmaUpdateRequest request) {
        Turma turma = buscarPorId(id);
        var empresa = empresaService.buscarPorId(request.empresaId());
        
        turma.setEmpresa(empresa);
        turma.setNome(request.nome());
        turma.setTurno(request.turno());
        turma.setValorBase(request.valorBase());
        turma.setCapacidade(request.capacidade());
        turma.setAtivo(request.ativo());
        
        turma = repository.save(turma);
        return toResponse(turma);
    }

    private TurmaResponse toResponse(Turma turma) {
        return new TurmaResponse(
                turma.getId(),
                turma.getEmpresa() != null ? turma.getEmpresa().getId() : null,
                turma.getEmpresa() != null ? turma.getEmpresa().getNome() : null,
                turma.getNome(),
                turma.getTurno(),
                turma.getValorBase(),
                turma.getCapacidade(),
                turma.getAtivo()
        );
    }
}

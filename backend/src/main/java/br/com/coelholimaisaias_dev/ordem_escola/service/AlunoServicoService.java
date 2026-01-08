package br.com.coelholimaisaias_dev.ordem_escola.service;

import br.com.coelholimaisaias_dev.ordem_escola.domain.AlunoServico;
import br.com.coelholimaisaias_dev.ordem_escola.repository.AlunoServicoRepository;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.AlunoServicoCreateRequest;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.AlunoServicoResponse;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.AlunoServicoUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlunoServicoService extends BaseService<AlunoServico> {

    private final AlunoServicoRepository repository;
    private final AlunoService alunoService;
    private final ServicoService servicoService;

    public AlunoServicoService(AlunoServicoRepository repository, AlunoService alunoService, ServicoService servicoService) {
        super(repository);
        this.repository = repository;
        this.alunoService = alunoService;
        this.servicoService = servicoService;
    }

    public List<AlunoServicoResponse> listarPorFiltro(Long alunoId, Long servicoId, Boolean ativo) {
        return repository.findAllByAtivoTrue()
                .stream()
                .filter(as -> alunoId == null || as.getAluno().getId().equals(alunoId))
                .filter(as -> servicoId == null || as.getServico().getId().equals(servicoId))
                .filter(as -> ativo == null || as.getAtivo().equals(ativo))
                .map(this::toResponse)
                .toList();
    }

    public AlunoServicoResponse buscarPorIdResponse(Long id) {
        AlunoServico alunoServico = buscarPorId(id);
        return toResponse(alunoServico);
    }

    @Transactional
    public AlunoServicoResponse criar(AlunoServicoCreateRequest request) {
        var aluno = alunoService.buscarPorId(request.alunoId());
        var servico = servicoService.buscarPorId(request.servicoId());
        
        var alunoServico = AlunoServico.builder()
                .aluno(aluno)
                .servico(servico)
                .dataInicio(request.dataInicio())
                .ativo(true)
                .build();
        
        alunoServico = repository.save(alunoServico);
        return toResponse(alunoServico);
    }

    @Transactional
    public AlunoServicoResponse atualizar(Long id, AlunoServicoUpdateRequest request) {
        AlunoServico alunoServico = buscarPorId(id);
        var aluno = alunoService.buscarPorId(request.alunoId());
        var servico = servicoService.buscarPorId(request.servicoId());
        
        alunoServico.setAluno(aluno);
        alunoServico.setServico(servico);
        alunoServico.setDataInicio(request.dataInicio());
        alunoServico.setAtivo(request.ativo());
        
        alunoServico = repository.save(alunoServico);
        return toResponse(alunoServico);
    }

    private AlunoServicoResponse toResponse(AlunoServico alunoServico) {
        return new AlunoServicoResponse(
                alunoServico.getId(),
                alunoServico.getAluno() != null ? alunoServico.getAluno().getId() : null,
                alunoServico.getAluno() != null ? alunoServico.getAluno().getUsuario().getNome() : null,
                alunoServico.getServico() != null ? alunoServico.getServico().getId() : null,
                alunoServico.getServico() != null ? alunoServico.getServico().getNome() : null,
                alunoServico.getDataInicio(),
                alunoServico.getAtivo()
        );
    }
}


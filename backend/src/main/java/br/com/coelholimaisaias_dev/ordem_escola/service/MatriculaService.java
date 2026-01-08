package br.com.coelholimaisaias_dev.ordem_escola.service;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Matricula;
import br.com.coelholimaisaias_dev.ordem_escola.repository.MatriculaRepository;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.MatriculaCreateRequest;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.MatriculaResponse;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.MatriculaUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MatriculaService extends BaseService<Matricula> {

    private final MatriculaRepository repository;
    private final AlunoService alunoService;
    private final TurmaService turmaService;

    public MatriculaService(MatriculaRepository repository, AlunoService alunoService, TurmaService turmaService) {
        super(repository);
        this.repository = repository;
        this.alunoService = alunoService;
        this.turmaService = turmaService;
    }

    public List<MatriculaResponse> listarPorFiltro(Long alunoId, Long turmaId, Boolean ativo) {
        return repository.findAllByAtivoTrue()
                .stream()
                .filter(m -> alunoId == null || m.getAluno().getId().equals(alunoId))
                .filter(m -> turmaId == null || m.getTurma().getId().equals(turmaId))
                .filter(m -> ativo == null || m.getAtivo().equals(ativo))
                .map(this::toResponse)
                .toList();
    }

    public MatriculaResponse buscarPorIdResponse(Long id) {
        Matricula matricula = buscarPorId(id);
        return toResponse(matricula);
    }

    @Transactional
    public MatriculaResponse criar(MatriculaCreateRequest request) {
        var aluno = alunoService.buscarPorId(request.alunoId());
        var turma = turmaService.buscarPorId(request.turmaId());
        
        var matricula = Matricula.builder()
                .aluno(aluno)
                .turma(turma)
                .dataMatricula(request.dataMatricula())
                .ativo(true)
                .build();
        
        matricula = repository.save(matricula);
        return toResponse(matricula);
    }

    @Transactional
    public MatriculaResponse atualizar(Long id, MatriculaUpdateRequest request) {
        Matricula matricula = buscarPorId(id);
        var aluno = alunoService.buscarPorId(request.alunoId());
        var turma = turmaService.buscarPorId(request.turmaId());
        
        matricula.setAluno(aluno);
        matricula.setTurma(turma);
        matricula.setDataMatricula(request.dataMatricula());
        matricula.setAtivo(request.ativo());
        matricula.setDataCancelamento(request.dataCancelamento());
        
        matricula = repository.save(matricula);
        return toResponse(matricula);
    }

    private MatriculaResponse toResponse(Matricula matricula) {
        return new MatriculaResponse(
                matricula.getId(),
                matricula.getAluno() != null ? matricula.getAluno().getId() : null,
                matricula.getAluno() != null ? matricula.getAluno().getUsuario().getNome() : null,
                matricula.getTurma() != null ? matricula.getTurma().getId() : null,
                matricula.getTurma() != null ? matricula.getTurma().getNome() : null,
                matricula.getDataMatricula(),
                matricula.getAtivo(),
                matricula.getDataCancelamento()
        );
    }
}


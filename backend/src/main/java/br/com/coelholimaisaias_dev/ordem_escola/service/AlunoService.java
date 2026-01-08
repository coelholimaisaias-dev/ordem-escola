package br.com.coelholimaisaias_dev.ordem_escola.service;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Aluno;
import br.com.coelholimaisaias_dev.ordem_escola.repository.AlunoRepository;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.AlunoCreateRequest;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.AlunoResponse;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.AlunoUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlunoService extends BaseService<Aluno> {

    private final AlunoRepository repository;
    private final UsuarioService usuarioService;

    public AlunoService(AlunoRepository repository, UsuarioService usuarioService) {
        super(repository);
        this.repository = repository;
        this.usuarioService = usuarioService;
    }

    public List<AlunoResponse> listarResponse() {
        return repository.findAllByAtivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<AlunoResponse> listarPorFiltro(String usuarioNome, Long usuarioId, Boolean ativo, Long empresaId) {
        return repository.findAllByAtivoTrue()
                .stream()
                .filter(a -> usuarioNome == null || a.getUsuario().getNome().toLowerCase().contains(usuarioNome.toLowerCase()))
                .filter(a -> usuarioId == null || a.getUsuario().getId().equals(usuarioId))
                .filter(a -> ativo == null || a.getAtivo().equals(ativo))
                .filter(a -> empresaId == null || (a.getUsuario().getEmpresa() != null && a.getUsuario().getEmpresa().getId().equals(empresaId)))
                .map(this::toResponse)
                .toList();
    }

    public AlunoResponse buscarPorIdResponse(Long id) {
        Aluno aluno = buscarPorId(id);
        return toResponse(aluno);
    }

    @Transactional
    public AlunoResponse criar(AlunoCreateRequest request) {
        var usuario = usuarioService.buscarPorId(request.usuarioId());
        
        var aluno = Aluno.builder()
                .usuario(usuario)
                .dataNascimento(request.dataNascimento())
                .ativo(true)
                .build();
        
        aluno = repository.save(aluno);
        return toResponse(aluno);
    }

    @Transactional
    public AlunoResponse atualizar(Long id, AlunoUpdateRequest request) {
        Aluno aluno = buscarPorId(id);
        var usuario = usuarioService.buscarPorId(request.usuarioId());
        
        aluno.setUsuario(usuario);
        aluno.setDataNascimento(request.dataNascimento());
        aluno.setAtivo(request.ativo());
        
        aluno = repository.save(aluno);
        return toResponse(aluno);
    }

    private AlunoResponse toResponse(Aluno aluno) {
        return new AlunoResponse(
                aluno.getId(),
                aluno.getUsuario() != null ? aluno.getUsuario().getId() : null,
                aluno.getUsuario() != null ? aluno.getUsuario().getNome() : null,
                aluno.getUsuario() != null ? aluno.getUsuario().getEmail() : null,
                aluno.getUsuario() != null && aluno.getUsuario().getEmpresa() != null ? aluno.getUsuario().getEmpresa().getId() : null,
                aluno.getDataNascimento(),
                aluno.getAtivo()
        );
    }

    public void validarUsuarioPertenceAEmpresa(Long usuarioId, Long empresaId) {
        var usuario = usuarioService.buscarPorId(usuarioId);
        if (usuario.getEmpresa() == null || !usuario.getEmpresa().getId().equals(empresaId)) {
            throw new IllegalStateException("Usuário não pertence à sua empresa");
        }
    }
}

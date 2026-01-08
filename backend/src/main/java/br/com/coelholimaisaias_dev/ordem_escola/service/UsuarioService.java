package br.com.coelholimaisaias_dev.ordem_escola.service;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Usuario;
import br.com.coelholimaisaias_dev.ordem_escola.repository.UsuarioRepository;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.UsuarioCreateRequest;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.UsuarioResponse;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.UsuarioUpdateRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService extends BaseService<Usuario> {

    private final UsuarioRepository repository;
    private final EmpresaService empresaService;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, EmpresaService empresaService, PasswordEncoder passwordEncoder) {
        super(repository);
        this.repository = repository;
        this.empresaService = empresaService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioResponse> listarPorFiltro(String nome, String email, Long empresaId, String perfil, Boolean ativo) {
        return repository.findAllByAtivoTrue()
                .stream()
                .filter(u -> nome == null || u.getNome().toLowerCase().contains(nome.toLowerCase()))
                .filter(u -> email == null || u.getEmail().toLowerCase().contains(email.toLowerCase()))
                .filter(u -> empresaId == null || u.getEmpresa().getId().equals(empresaId))
                .filter(u -> perfil == null || u.getPerfil().toString().equals(perfil))
                .filter(u -> ativo == null || u.getAtivo().equals(ativo))
                .map(this::toResponse)
                .toList();
    }

    public UsuarioResponse buscarPorIdResponse(Long id) {
        Usuario usuario = buscarPorId(id);
        return toResponse(usuario);
    }

    @Transactional
    public UsuarioResponse criar(UsuarioCreateRequest request) {
        var empresa = empresaService.buscarPorId(request.empresaId());
        
        var usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(passwordEncoder.encode(request.senha()))
                .empresa(empresa)
                .perfil(request.perfil())
                .ativo(true)
                .build();
        
        usuario = repository.save(usuario);
        return toResponse(usuario);
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioUpdateRequest request) {
        Usuario usuario = buscarPorId(id);
        var empresa = empresaService.buscarPorId(request.empresaId());
        
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setEmpresa(empresa);
        usuario.setPerfil(request.perfil());
        usuario.setAtivo(request.ativo());
        
        usuario = repository.save(usuario);
        return toResponse(usuario);
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getEmpresa() != null ? usuario.getEmpresa().getId() : null,
                usuario.getEmpresa() != null ? usuario.getEmpresa().getNome() : null,
                usuario.getPerfil(),
                usuario.getAtivo()
        );
    }
}

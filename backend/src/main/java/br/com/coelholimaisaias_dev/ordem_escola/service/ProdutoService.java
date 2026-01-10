package br.com.coelholimaisaias_dev.ordem_escola.service;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Produto;
import br.com.coelholimaisaias_dev.ordem_escola.repository.ProdutoRepository;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.ProdutoCreateRequest;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.ProdutoResponse;
import br.com.coelholimaisaias_dev.ordem_escola.web.dto.ProdutoUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoService extends BaseService<Produto> {

    private final ProdutoRepository repository;
    private final EmpresaService empresaService;

    public ProdutoService(ProdutoRepository repository, EmpresaService empresaService) {
        super(repository);
        this.repository = repository;
        this.empresaService = empresaService;
    }

    public List<ProdutoResponse> listarPorFiltro(String nome, String tipo, Boolean ativo, Long empresaId) {
        return repository.findAllByAtivoTrue()
                .stream()
                .filter(p -> nome == null || p.getNome().toLowerCase().contains(nome.toLowerCase()))
                .filter(p -> tipo == null || p.getTipo().name().equals(tipo))
                .filter(p -> ativo == null || p.getAtivo().equals(ativo))
                .filter(p -> empresaId == null || (p.getEmpresa() != null && p.getEmpresa().getId().equals(empresaId)))
                .map(this::toResponse)
                .toList();
    }

    public List<ProdutoResponse> listarResponse() {
        return repository.findAllByAtivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProdutoResponse buscarPorIdResponse(Long id) {
        Produto produto = buscarPorId(id);
        return toResponse(produto);
    }

    @Transactional
    public ProdutoResponse criar(ProdutoCreateRequest request) {
        var empresa = empresaService.buscarPorId(request.empresaId());

        var produto = Produto.builder()
                .empresa(empresa)
                .nome(request.nome())
                .descricao(request.descricao())
                .tipo(request.tipo())
                .valorUnitario(request.valorUnitario())
                .ativo(true)
                .build();

        produto = repository.save(produto);
        return toResponse(produto);
    }

    @Transactional
    public ProdutoResponse atualizar(Long id, ProdutoUpdateRequest request) {
        Produto produto = buscarPorId(id);

        produto.setNome(request.nome());
        produto.setDescricao(request.descricao());
        produto.setTipo(request.tipo());
        produto.setValorUnitario(request.valorUnitario());
        produto.setAtivo(request.ativo());

        produto = repository.save(produto);
        return toResponse(produto);
    }

    private ProdutoResponse toResponse(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getEmpresa() != null ? produto.getEmpresa().getId() : null,
                produto.getEmpresa() != null ? produto.getEmpresa().getNome() : null,
                produto.getNome(),
                produto.getDescricao(),
                produto.getTipo(),
                produto.getValorUnitario(),
                produto.getAtivo()
        );
    }
}

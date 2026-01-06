package br.com.coelholimaisaias_dev.ordem_escola.service;

import br.com.coelholimaisaias_dev.ordem_escola.domain.BaseEntity;
import br.com.coelholimaisaias_dev.ordem_escola.repository.BaseRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public abstract class BaseService<T extends BaseEntity> {

    protected final BaseRepository<T> repository;

    protected BaseService(BaseRepository<T> repository) {
        this.repository = repository;
    }

    // Listar todos ativos
    public List<T> listar() {
        return repository.findAllByAtivoTrue();
    }

    // Buscar por ID (somente ativos)
    public T buscarPorId(Long id) {
        return repository.findById(id)
                .filter(BaseEntity::getAtivo)
                .orElseThrow(() -> new RuntimeException("Registro não encontrado"));
    }

    // Criar / atualizar
    @Transactional
    public T salvar(T entity) {
        return repository.save(entity);
    }

    // Delete lógico
    @Transactional
    public void deletar(Long id) {
        T entity = buscarPorId(id);
        repository.deleteLogic(entity);
    }
}

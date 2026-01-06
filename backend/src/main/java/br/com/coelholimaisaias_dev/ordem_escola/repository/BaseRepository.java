package br.com.coelholimaisaias_dev.ordem_escola.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

import br.com.coelholimaisaias_dev.ordem_escola.domain.BaseEntity;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long> {
    List<T> findAllByAtivoTrue();

    default void deleteLogic(T entity) {
        try {
            var method = entity.getClass().getMethod("setAtivo", Boolean.class);
            method.invoke(entity, false);
            save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao realizar delete lógico", e);
        }
    }
}

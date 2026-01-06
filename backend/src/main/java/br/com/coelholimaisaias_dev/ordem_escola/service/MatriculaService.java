package br.com.coelholimaisaias_dev.ordem_escola.service;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Matricula;
import br.com.coelholimaisaias_dev.ordem_escola.repository.MatriculaRepository;
import org.springframework.stereotype.Service;

@Service
public class MatriculaService extends BaseService<Matricula> {

    public MatriculaService(MatriculaRepository repository) {
        super(repository);
    }
}

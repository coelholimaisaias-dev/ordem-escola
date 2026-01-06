package br.com.coelholimaisaias_dev.ordem_escola.service;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Servico;
import br.com.coelholimaisaias_dev.ordem_escola.repository.ServicoRepository;
import org.springframework.stereotype.Service;

@Service
public class ServicoService extends BaseService<Servico> {

    public ServicoService(ServicoRepository repository) {
        super(repository);
    }
}

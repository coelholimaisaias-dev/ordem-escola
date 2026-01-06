package br.com.coelholimaisaias_dev.ordem_escola.service;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Empresa;
import br.com.coelholimaisaias_dev.ordem_escola.repository.EmpresaRepository;
import org.springframework.stereotype.Service;

@Service
public class EmpresaService extends BaseService<Empresa> {

    public EmpresaService(EmpresaRepository repository) {
        super(repository);
    }
}

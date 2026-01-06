package br.com.coelholimaisaias_dev.ordem_escola.service;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Usuario;
import br.com.coelholimaisaias_dev.ordem_escola.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService extends BaseService<Usuario> {

    public UsuarioService(UsuarioRepository repository) {
        super(repository);
    }
}

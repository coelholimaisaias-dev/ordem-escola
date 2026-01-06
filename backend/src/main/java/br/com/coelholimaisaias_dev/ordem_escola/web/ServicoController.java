package br.com.coelholimaisaias_dev.ordem_escola.web;

import br.com.coelholimaisaias_dev.ordem_escola.domain.Servico;
import br.com.coelholimaisaias_dev.ordem_escola.service.ServicoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/servicos")
public class ServicoController extends BaseController<Servico> {

    public ServicoController(ServicoService service) {
        super(service);
    }
}

package br.com.rodrigo.api.controleestoque.service.factory;

import br.com.rodrigo.api.controleestoque.conversor.UnidadeMapper;
import br.com.rodrigo.api.controleestoque.model.Unidade;
import br.com.rodrigo.api.controleestoque.service.IUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnidadeUsuarioLogadoFactory implements UnidadeFactory {
    private final IUsuario usuarioService;

    @Override
    public Unidade criarUnidade() {
        return UnidadeMapper.responseParaEntidade(usuarioService.obterUsuarioLogado().getUnidade());
    }
}

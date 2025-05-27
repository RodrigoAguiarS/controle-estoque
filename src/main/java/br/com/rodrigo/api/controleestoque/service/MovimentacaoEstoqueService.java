package br.com.rodrigo.api.controleestoque.service;

import br.com.rodrigo.api.controleestoque.model.Produto;
import br.com.rodrigo.api.controleestoque.model.TipoMovimentacao;
import br.com.rodrigo.api.controleestoque.model.TipoOperacao;
import br.com.rodrigo.api.controleestoque.service.strategy.MovimentacaoEstoqueStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MovimentacaoEstoqueService {

    private final Map<TipoMovimentacao, MovimentacaoEstoqueStrategy> estrategias;

    public void processarMovimentacao(Produto produto, TipoMovimentacao tipo, TipoOperacao operacao, int quantidade,
                                      BigDecimal valor) {
        estrategias.get(tipo).executar(produto, quantidade, operacao, valor);
    }
}

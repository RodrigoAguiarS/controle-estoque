package br.com.rodrigo.api.controleestoque.service;

import br.com.rodrigo.api.controleestoque.model.Produto;
import br.com.rodrigo.api.controleestoque.model.TipoOperacao;

import java.math.BigDecimal;

public interface MovimentacaoEstoqueStrategy {
    void executar(Produto produto, int quantidade, TipoOperacao operacao, BigDecimal valor);
}

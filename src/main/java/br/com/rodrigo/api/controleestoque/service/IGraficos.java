package br.com.rodrigo.api.controleestoque.service;

import br.com.rodrigo.api.controleestoque.model.MovimentacaoEstoque;
import br.com.rodrigo.api.controleestoque.model.response.ProdutoLucroResponse;
import br.com.rodrigo.api.controleestoque.model.response.TipoProdutoEstoqueResponse;

import java.util.List;

public interface IGraficos {

    List<ProdutoLucroResponse> buscarLucroPorProduto();
    List<TipoProdutoEstoqueResponse> buscarEstoquePorTipoProduto();
    public List<MovimentacaoEstoque> buscarUltimasMovimentacoes(int limite);

}

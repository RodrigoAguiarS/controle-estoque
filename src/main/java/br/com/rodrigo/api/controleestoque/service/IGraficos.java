package br.com.rodrigo.api.controleestoque.service;

import br.com.rodrigo.api.controleestoque.model.MovimentacaoEstoque;
import br.com.rodrigo.api.controleestoque.model.response.CaixaDetalhesResponse;
import br.com.rodrigo.api.controleestoque.model.response.ProdutoLucroResponse;
import br.com.rodrigo.api.controleestoque.model.response.TipoProdutoEstoqueResponse;
import br.com.rodrigo.api.controleestoque.model.response.VendasPorFormaPagamentoResponse;
import br.com.rodrigo.api.controleestoque.model.response.VendasPorUnidadeResponse;

import java.util.List;

public interface IGraficos {

    List<ProdutoLucroResponse> buscarLucroPorProduto();
    List<TipoProdutoEstoqueResponse> buscarEstoquePorTipoProduto();
    List<MovimentacaoEstoque> buscarUltimasMovimentacoes(int limite);
    CaixaDetalhesResponse buscarInformacoesCaixaUsuarioLogado();
    List<VendasPorUnidadeResponse> buscarVendasPorUnidade();
    List<VendasPorFormaPagamentoResponse> buscarVendasPorFormaPagamento();

}

package br.com.rodrigo.api.controleestoque.service.impl;

import br.com.rodrigo.api.controleestoque.model.MovimentacaoEstoque;
import br.com.rodrigo.api.controleestoque.model.response.ProdutoLucroResponse;
import br.com.rodrigo.api.controleestoque.model.response.TipoProdutoEstoqueResponse;
import br.com.rodrigo.api.controleestoque.repository.MovimentacaoEstoqueRepository;
import br.com.rodrigo.api.controleestoque.repository.ProdutoRepository;
import br.com.rodrigo.api.controleestoque.service.IGraficos;
import br.com.rodrigo.api.controleestoque.service.factory.UnidadeUsuarioLogadoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GraficoServiceImpl implements IGraficos {

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
    private final UnidadeUsuarioLogadoFactory unidadeFactory;

    private Long getIdUnidade() {
        return unidadeFactory.criarUnidade().getId();
    }

    @Override
    public List<ProdutoLucroResponse> buscarLucroPorProduto() {
        return produtoRepository.buscarLucroPorProduto(getIdUnidade());
    }

    @Override
    public List<TipoProdutoEstoqueResponse> buscarEstoquePorTipoProduto() {
        return produtoRepository.buscarEstoquePorTipoProduto(getIdUnidade());
    }

    @Override
    public List<MovimentacaoEstoque> buscarUltimasMovimentacoes(int limite) {
        Pageable pageable = PageRequest.of(0, limite);
        return movimentacaoEstoqueRepository.buscarUltimasMovimentacoes(getIdUnidade(), pageable);
    }
}

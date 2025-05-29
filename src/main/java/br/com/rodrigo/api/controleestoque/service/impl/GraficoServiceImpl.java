package br.com.rodrigo.api.controleestoque.service.impl;

import br.com.rodrigo.api.controleestoque.model.MovimentacaoEstoque;
import br.com.rodrigo.api.controleestoque.model.response.CaixaDetalhesResponse;
import br.com.rodrigo.api.controleestoque.model.response.ProdutoLucroResponse;
import br.com.rodrigo.api.controleestoque.model.response.TipoProdutoEstoqueResponse;
import br.com.rodrigo.api.controleestoque.model.response.VendasPorFormaPagamentoResponse;
import br.com.rodrigo.api.controleestoque.model.response.VendasPorUnidadeResponse;
import br.com.rodrigo.api.controleestoque.repository.CaixaRepository;
import br.com.rodrigo.api.controleestoque.repository.MovimentacaoEstoqueRepository;
import br.com.rodrigo.api.controleestoque.repository.ProdutoRepository;
import br.com.rodrigo.api.controleestoque.repository.VendaRepository;
import br.com.rodrigo.api.controleestoque.service.IGraficos;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static br.com.rodrigo.api.controleestoque.service.singleton.UsuarioContext.getUsuarioLogado;

@Service
@RequiredArgsConstructor
public class GraficoServiceImpl implements IGraficos {

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
    private final CaixaRepository caixaRepository;
    private final VendaRepository vendaRepository;


    @Override
    public List<ProdutoLucroResponse> buscarLucroPorProduto() {
        return produtoRepository.buscarLucroPorProduto(getUsuarioLogado().getUnidade().getId());
    }

    @Override
    public List<TipoProdutoEstoqueResponse> buscarEstoquePorTipoProduto() {
        return produtoRepository.buscarEstoquePorTipoProduto(getUsuarioLogado().getUnidade().getId());
    }

    @Override
    public List<MovimentacaoEstoque> buscarUltimasMovimentacoes(int limite) {
        Pageable pageable = PageRequest.of(0, limite);
        return movimentacaoEstoqueRepository.buscarUltimasMovimentacoes(getUsuarioLogado().getUnidade().getId(), pageable);
    }

    @Override
    public CaixaDetalhesResponse buscarInformacoesCaixaUsuarioLogado() {
        return caixaRepository.findCaixaInfoByUsuarioId(getUsuarioLogado().getId())
                .orElse(CaixaDetalhesResponse.builder()
                        .valorAtual(BigDecimal.ZERO)
                        .valorEntrada(BigDecimal.ZERO)
                        .valorSaida(BigDecimal.ZERO)
                        .dataAbertura(LocalDateTime.now())
                        .ativo(false)
                        .build());
    }

    @Override
    public List<VendasPorUnidadeResponse> buscarVendasPorUnidade() {
        return vendaRepository.buscarVendasPorUnidade();
    }


    @Override
    public List<VendasPorFormaPagamentoResponse> buscarVendasPorFormaPagamento() {
        return vendaRepository.buscarVendasPorFormaPagamento(getUsuarioLogado().getUnidade().getId());
    }
}

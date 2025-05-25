package br.com.rodrigo.api.controleestoque.services;

import br.com.rodrigo.api.controleestoque.exception.ViolacaoIntegridadeDadosException;
import br.com.rodrigo.api.controleestoque.model.Produto;
import br.com.rodrigo.api.controleestoque.model.TipoMovimentacao;
import br.com.rodrigo.api.controleestoque.model.TipoOperacao;
import br.com.rodrigo.api.controleestoque.repository.MovimentacaoEstoqueRepository;
import br.com.rodrigo.api.controleestoque.repository.ProdutoRepository;
import br.com.rodrigo.api.controleestoque.service.MovimentacaoEstoqueStrategy;
import br.com.rodrigo.api.controleestoque.service.impl.CadastroProdutoStrategy;
import br.com.rodrigo.api.controleestoque.service.impl.MovimentacaoEstoqueService;
import br.com.rodrigo.api.controleestoque.service.impl.SaidaVendaStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


class MovimentacaoEstoqueServiceTest {

    @Mock
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private MovimentacaoEstoqueService movimentacaoEstoqueService;

    @Mock
    private Map<TipoMovimentacao, MovimentacaoEstoqueStrategy> estrategias;

    @Mock
    private MovimentacaoEstoqueStrategy estrategiaEntrada;

    @Mock
    private MovimentacaoEstoqueStrategy estrategiaSaida;

    private Produto produto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        produto = Produto.builder()
                .descricao("Produto Teste")
                .quantidadeEstoque(50)
                .valorFornecedor(new BigDecimal("100.00"))
                .build();

        estrategiaEntrada = new CadastroProdutoStrategy(movimentacaoEstoqueRepository, produtoRepository);

        estrategiaSaida = new SaidaVendaStrategy(movimentacaoEstoqueRepository, produtoRepository);

        when(estrategias.get(TipoMovimentacao.ENTRADA)).thenReturn(estrategiaEntrada);
        when(estrategias.get(TipoMovimentacao.SAIDA)).thenReturn(estrategiaSaida);
    }

    @Test
    void deveProcessarMovimentacaoEntradaComQuantidadePositiva() {
        int quantidadeMovimentacao = 10;
        BigDecimal valorMovimentacao = new BigDecimal("1000.00");

        movimentacaoEstoqueService.processarMovimentacao(
                produto,
                TipoMovimentacao.ENTRADA,
                TipoOperacao.CADASTRO_PRODUTO,
                quantidadeMovimentacao,
                valorMovimentacao
        );
        assertEquals(60, produto.getQuantidadeEstoque(), "A quantidade no estoque deve ser atualizada corretamente.");
    }

    @Test
    void naoDeveProcessarMovimentacaoEntradaComQuantidadeZero() {
        int quantidadeMovimentacao = 0;
        BigDecimal valorMovimentacao = new BigDecimal("0.00");

        movimentacaoEstoqueService.processarMovimentacao(
                produto,
                TipoMovimentacao.ENTRADA,
                TipoOperacao.CADASTRO_PRODUTO,
                quantidadeMovimentacao,
                valorMovimentacao
        );

        assertEquals(50, produto.getQuantidadeEstoque());
    }

    @Test
    void deveProcessarMovimentacaoSaidaComQuantidadeValida() {
        int quantidadeMovimentacao = 20;
        BigDecimal valorMovimentacao = new BigDecimal("2000.00");

        movimentacaoEstoqueService.processarMovimentacao(
                produto,
                TipoMovimentacao.SAIDA,
                TipoOperacao.VENDA,
                quantidadeMovimentacao,
                valorMovimentacao
        );
        assertEquals(30, produto.getQuantidadeEstoque());
    }

    @Test
    void deveLancarExcecaoParaMovimentacaoSaidaComQuantidadeMaiorQueEstoque() {
        int quantidadeMovimentacao = 60;
        BigDecimal valorMovimentacao = new BigDecimal("6000.00");

        assertThrows(ViolacaoIntegridadeDadosException.class, () ->
                movimentacaoEstoqueService.processarMovimentacao(
                produto,
                TipoMovimentacao.SAIDA,
                TipoOperacao.VENDA,
                quantidadeMovimentacao,
                valorMovimentacao
        ));
        assertEquals(50, produto.getQuantidadeEstoque());
    }

    @Test
    void naoDeveProcessarMovimentacaoSaidaComQuantidadeZero() {
        int quantidadeMovimentacao = 0;
        BigDecimal valorMovimentacao = new BigDecimal("0.00");

        movimentacaoEstoqueService.processarMovimentacao(
                produto,
                TipoMovimentacao.SAIDA,
                TipoOperacao.VENDA,
                quantidadeMovimentacao,
                valorMovimentacao
        );
        assertEquals(50, produto.getQuantidadeEstoque());
    }
}

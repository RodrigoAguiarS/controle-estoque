package br.com.rodrigo.api.controleestoque.service.impl;

import br.com.rodrigo.api.controleestoque.exception.MensagensError;
import br.com.rodrigo.api.controleestoque.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.controleestoque.model.Produto;
import br.com.rodrigo.api.controleestoque.model.TipoMovimentacao;
import br.com.rodrigo.api.controleestoque.model.TipoOperacao;
import br.com.rodrigo.api.controleestoque.model.form.ProdutoForm;
import br.com.rodrigo.api.controleestoque.model.response.ProdutoResponse;
import br.com.rodrigo.api.controleestoque.model.response.TipoProdutoResponse;
import br.com.rodrigo.api.controleestoque.repository.ProdutoRepository;
import br.com.rodrigo.api.controleestoque.service.IProduto;
import br.com.rodrigo.api.controleestoque.service.ITipoProduto;
import br.com.rodrigo.api.controleestoque.service.S3StorageService;
import br.com.rodrigo.api.controleestoque.service.strategy.MovimentacaoEstoqueService;
import br.com.rodrigo.api.controleestoque.service.template.CalculoValorVendaTemplate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static br.com.rodrigo.api.controleestoque.conversor.ProdutoMapper.entidadeParaResponse;
import static br.com.rodrigo.api.controleestoque.conversor.TipoProdutoMapper.responseParaEntidade;
import static br.com.rodrigo.api.controleestoque.service.singleton.UsuarioContext.getUsuarioLogado;
import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements IProduto {

    private final ProdutoRepository produtoRepository;
    private final ITipoProduto tipoProdutoService;
    private final S3StorageService s3StorageService;
    private final MovimentacaoEstoqueService movimentacaoService;
    private final CalculoValorVendaTemplate calculoValorVenda;


    @Override
    @Transactional
    public ProdutoResponse criar(Long idProduto, ProdutoForm produtoForm) {
        Produto produto = criaProduto(produtoForm, idProduto);
        produto = produtoRepository.save(produto);
        return construirDto(produto);
    }

    @Override
    public void deletar(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.PRODUTO_NAO_ENCONTRADO.getMessage(id)));

        if (isNotEmpty(produto.getArquivosUrl())) {
            for (String fileUrl : produto.getArquivosUrl()) {
                s3StorageService.apagarArquivo(fileUrl);
            }
        }
        produto.desativar();
        produtoRepository.save(produto);
    }

    @Override
    public Optional<ProdutoResponse> consultarPorId(Long id) {
        return produtoRepository.findById(id).map(this::construirDto);
    }

    @Override
    public Page<ProdutoResponse> listarTodos(int page, int size, String sort, Long id, String descricao, Long tipoProdutoId, BigDecimal valorFornecedor, Integer quantidadeEstoque) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort != null ? sort : "id"));
        Page<Produto> produtos = produtoRepository.findAll(id, descricao, valorFornecedor, quantidadeEstoque, tipoProdutoId, getUsuarioLogado().getUnidade().getId(), pageable);
        return produtos.map(this::construirDto);
    }

    private Produto criaProduto(ProdutoForm produtoForm, Long id) {

        Produto produto = id == null ? new Produto() : produtoRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.PRODUTO_NAO_ENCONTRADO.getMessage(id)));

        TipoProdutoResponse tipoProduto = tipoProdutoService.consultarPorId(produtoForm.getTipoProdutoId())
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.TIPO_PRODUTO_NAO_ENCONTRADO.getMessage(produtoForm.getTipoProdutoId())));


        BigDecimal valorFornecedor = produtoForm.getValorFornecedor();
        produto.setTipoProduto(responseParaEntidade(tipoProduto));
        BigDecimal valorVenda = calcularValorVenda(valorFornecedor);

        produto.setTipoProduto(responseParaEntidade(tipoProduto));
        produto.setUnidade(getUsuarioLogado().getUnidade());
        produto.setValorFornecedor(produtoForm.getValorFornecedor());
        produto.setArquivosUrl(produtoForm.getArquivosUrl());
        produto.setValorVenda(valorVenda);
        produto.setDescricao(produtoForm.getDescricao());
        processarMovimentacaoEstoque(produto, produtoForm);

        return produto;
    }

    private void processarMovimentacaoEstoque(Produto produto, ProdutoForm produtoForm) {
        int quantidadeAnterior = produto.getQuantidadeEstoque() != null ?
                produto.getQuantidadeEstoque() : BigDecimal.ZERO.intValue();
        int novaQuantidade = produtoForm.getQuantidadeEstoque();
        int novaDiferenca = novaQuantidade - quantidadeAnterior;

        if (novaDiferenca != BigDecimal.ZERO.intValue()) {
            TipoMovimentacao tipo = novaDiferenca > BigDecimal.ZERO.intValue() ?
                    TipoMovimentacao.ENTRADA : TipoMovimentacao.SAIDA;
            int quantidadeMovimentacao = Math.abs(novaDiferenca);
            BigDecimal valorMovimentacao = BigDecimal.valueOf(quantidadeMovimentacao)
                    .multiply(produtoForm.getValorFornecedor());

            movimentacaoService.processarMovimentacao(
                    produto,
                    tipo,
                    TipoOperacao.CADASTRO_PRODUTO,
                    quantidadeMovimentacao,
                    valorMovimentacao
            );

            produto.setQuantidadeEstoque(novaQuantidade);
        }
    }

    private BigDecimal calcularValorVenda(BigDecimal valorFornecedor) {
        BigDecimal fatorMultiplicacao = calculoValorVenda.calcularValorVenda(valorFornecedor)
                .divide(new BigDecimal("100.00"), 4, RoundingMode.HALF_UP)
                .add(BigDecimal.ONE);

        return valorFornecedor.multiply(fatorMultiplicacao)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private ProdutoResponse construirDto(Produto produto) {
        return entidadeParaResponse(produto);
    }
}

package br.com.rodrigo.api.controleestoque.service.impl;

import br.com.rodrigo.api.controleestoque.conversor.ProdutoMapper;
import br.com.rodrigo.api.controleestoque.conversor.TipoProdutoMapper;
import br.com.rodrigo.api.controleestoque.exception.MensagensError;
import br.com.rodrigo.api.controleestoque.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.controleestoque.model.Produto;
import br.com.rodrigo.api.controleestoque.model.form.ProdutoForm;
import br.com.rodrigo.api.controleestoque.model.response.ProdutoResponse;
import br.com.rodrigo.api.controleestoque.model.response.TipoProdutoResponse;
import br.com.rodrigo.api.controleestoque.repository.ProdutoRepository;
import br.com.rodrigo.api.controleestoque.service.IProduto;
import br.com.rodrigo.api.controleestoque.service.ITipoProduto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements IProduto {

    private final ProdutoRepository produtoRepository;
    private final TipoProdutoMapper tipoProdutoMapper;
    private final ProdutoMapper produtoMapper;
    private final ITipoProduto tipoProdutoService;

    @Override
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
        Page<Produto> produtos = produtoRepository.findAll(id, descricao, valorFornecedor, quantidadeEstoque, tipoProdutoId, pageable);
        return produtos.map(this::construirDto);
    }

    private Produto criaProduto(ProdutoForm produtoForm, Long id) {

        Produto produto = id == null ? new Produto() : produtoRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.PRODUTO_NAO_ENCONTRADO.getMessage(id)));

        TipoProdutoResponse tipoProduto = tipoProdutoService.consultarPorId(produtoForm.getTipoProdutoId())
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.TIPO_PRODUTO_NAO_ENCONTRADO.getMessage(produtoForm.getTipoProdutoId())));

        produto.setDescricao(produtoForm.getDescricao());
        produto.setTipoProduto(tipoProdutoMapper.responseParaEntidade(tipoProduto));
        produto.setValorFornecedor(produtoForm.getValorFornecedor());
        produto.setQuantidadeEstoque(produtoForm.getQuantidadeEstoque());

        return produto;
    }

    private ProdutoResponse construirDto(Produto produto) {
        return produtoMapper.entidadeParaResponse(produto);
    }
}

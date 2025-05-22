package br.com.rodrigo.api.controleestoque.service;

import br.com.rodrigo.api.controleestoque.model.Produto;
import br.com.rodrigo.api.controleestoque.model.form.ProdutoForm;
import br.com.rodrigo.api.controleestoque.model.response.ProdutoResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Optional;

public interface IProduto {
    ProdutoResponse criar(Long idProduto, ProdutoForm produtoForm);
    void deletar(Long id);
    Optional<ProdutoResponse> consultarPorId(Long id);
    Page<ProdutoResponse> listarTodos(int page, int size, String sort, Long id, String descricao,
                                      Long tipoProduto, BigDecimal valorFornecedor, Integer quantidadeEstoque);
}
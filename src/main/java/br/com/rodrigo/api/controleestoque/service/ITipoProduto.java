package br.com.rodrigo.api.controleestoque.service;

import br.com.rodrigo.api.controleestoque.model.form.TipoProdutoForm;
import br.com.rodrigo.api.controleestoque.model.response.TipoProdutoResponse;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ITipoProduto {
    TipoProdutoResponse criar(TipoProdutoForm tipoProdutoForm);
    TipoProdutoResponse atualizar(Long id, TipoProdutoForm tipoProdutoForm);
    void deletar(Long id);
    Optional<TipoProdutoResponse> consultarPorId(Long id);
    Page<TipoProdutoResponse> listarTodos(int page, int size, String sort, Long id, String nome,
                                          String descricao);
}

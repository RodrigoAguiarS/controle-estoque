package br.com.rodrigo.api.controleestoque.service;

import br.com.rodrigo.api.controleestoque.model.form.UnidadeForm;
import br.com.rodrigo.api.controleestoque.model.response.UnidadeResponse;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface  IUnidade {
    UnidadeResponse criar(Long id, UnidadeForm unidadeForm);
    Optional<UnidadeResponse> buscarPorId(Long id);
    Page<UnidadeResponse> listarTodos(int page, int size, String sort, Long id, String nome, Long idEmpresa);
    void deletar(Long id);
}

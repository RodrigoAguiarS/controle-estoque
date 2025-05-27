package br.com.rodrigo.api.controleestoque.service;

import br.com.rodrigo.api.controleestoque.model.form.PerfilForm;
import br.com.rodrigo.api.controleestoque.model.response.PerfilResponse;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IPerfil {
    PerfilResponse criar(Long id, PerfilForm perfilForm);
    void deletar(Long id);
    Optional<PerfilResponse> consultarPorId(Long id);
    Page<PerfilResponse> consultarTodos(int page, int size, String sort, Long id, String nome,
                                        String descricao);
}

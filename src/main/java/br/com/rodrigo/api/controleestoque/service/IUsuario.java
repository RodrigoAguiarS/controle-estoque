package br.com.rodrigo.api.controleestoque.service;

import br.com.rodrigo.api.controleestoque.model.form.UsuarioForm;
import br.com.rodrigo.api.controleestoque.model.form.UsuarioResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IUsuario {
    UsuarioResponse criar(Long id, UsuarioForm usuarioForm);
    Optional<UsuarioResponse> buscarPorId(Long id);
    Page<UsuarioResponse> listarTodos(int page, int size, String sort, String nome, String email, Long idPerfil, Long idUnidade);
    void deletar(Long id);
    List<String> obterPerfis(String email);
    UsuarioResponse obterUsuarioLogado();
}

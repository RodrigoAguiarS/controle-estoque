package br.com.rodrigo.api.controleestoque.service;

import br.com.rodrigo.api.controleestoque.model.form.EmpresaForm;
import br.com.rodrigo.api.controleestoque.model.response.EmpresaResponse;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IEmpresa {
    EmpresaResponse criar(Long id, EmpresaForm empresaForm);
    Optional<EmpresaResponse> buscarPorId(Long id);
    Page<EmpresaResponse> listarTodos(int page, int size, String sort, Long id, String nome, String cnpj);
    void deletar(Long id);
}

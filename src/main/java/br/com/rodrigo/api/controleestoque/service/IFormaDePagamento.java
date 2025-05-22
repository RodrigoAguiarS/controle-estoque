package br.com.rodrigo.api.controleestoque.service;

import br.com.rodrigo.api.controleestoque.model.form.FormaDePagamentoForm;
import br.com.rodrigo.api.controleestoque.model.response.FormaDePagamentoResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Optional;

public interface IFormaDePagamento {
    FormaDePagamentoResponse criar(Long id, FormaDePagamentoForm formaDePagamentoForm);
    void deletar(Long id);
    Optional<FormaDePagamentoResponse> consultarPorId(Long id);
    Page<FormaDePagamentoResponse> listarTodos(int page, int size, String sort, Long id,
                                               String nome, String descricao, BigDecimal porcentagemAcrescimo);
}

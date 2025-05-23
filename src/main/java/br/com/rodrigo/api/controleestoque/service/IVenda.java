package br.com.rodrigo.api.controleestoque.service;

import br.com.rodrigo.api.controleestoque.model.form.VendaForm;
import br.com.rodrigo.api.controleestoque.model.response.VendaResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface IVenda {

    VendaResponse realizarVenda(VendaForm vendaForm);
    Optional<VendaResponse> buscarPorId(Long idVenda);
    Page<VendaResponse> listarTodos(
            int page,
            int size,
            String sort,
            Long id,
            BigDecimal valorMinimo,
            BigDecimal valorMaximo,
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            Long formaDePagamentoId,
            Boolean ativo
    );

    BigDecimal calcularLucroVenda(Long idVenda);

    boolean validarEstoque(VendaForm vendaForm);

    void cancelarVenda(Long idVenda);
}

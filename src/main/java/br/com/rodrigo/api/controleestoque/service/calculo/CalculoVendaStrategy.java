package br.com.rodrigo.api.controleestoque.service.calculo;

import br.com.rodrigo.api.controleestoque.model.Venda;

import java.math.BigDecimal;

public interface CalculoVendaStrategy {
    BigDecimal calcular(Venda venda);
}

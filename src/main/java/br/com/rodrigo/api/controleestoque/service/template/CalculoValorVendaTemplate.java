package br.com.rodrigo.api.controleestoque.service.template;

import java.math.BigDecimal;

public abstract class CalculoValorVendaTemplate {

    public BigDecimal calcularValorVenda(BigDecimal valorFornecedor) {
        BigDecimal margem = obterMargemLucro();
        return valorFornecedor.add(valorFornecedor.multiply(margem));
    }

    protected abstract BigDecimal obterMargemLucro();
}
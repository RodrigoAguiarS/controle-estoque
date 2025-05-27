package br.com.rodrigo.api.controleestoque.service.template;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CalculoPadrao extends CalculoValorVendaTemplate {

    @Override
    protected BigDecimal obterMargemLucro() {
        return new BigDecimal("0.35");
    }
}

package br.com.rodrigo.api.controleestoque.service.calculo;

import br.com.rodrigo.api.controleestoque.model.ItemVenda;
import br.com.rodrigo.api.controleestoque.model.Venda;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CalculoValorTotalComAcrescimoStrategy implements CalculoVendaStrategy {
    @Override
    public BigDecimal calcular(Venda venda) {
        BigDecimal valorTotal = venda.getItens().stream()
                .map(ItemVenda::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal acrescimo = venda.getFormaDePagamento().getPorcentagemAcrescimo()
                .divide(new BigDecimal("100.00"), 2, RoundingMode.HALF_UP);
        BigDecimal valorAcrescimo = valorTotal.multiply(acrescimo);

        return valorTotal.add(valorAcrescimo).setScale(2, RoundingMode.HALF_UP);
    }
}

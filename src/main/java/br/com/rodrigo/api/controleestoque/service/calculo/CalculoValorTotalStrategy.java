package br.com.rodrigo.api.controleestoque.service.calculo;

import br.com.rodrigo.api.controleestoque.model.ItemVenda;
import br.com.rodrigo.api.controleestoque.model.Venda;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CalculoValorTotalStrategy implements CalculoVendaStrategy {
    @Override
    public BigDecimal calcular(Venda venda) {
        return venda.getItens().stream()
                .map(ItemVenda::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

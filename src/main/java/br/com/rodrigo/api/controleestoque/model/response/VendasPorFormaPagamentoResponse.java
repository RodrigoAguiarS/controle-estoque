package br.com.rodrigo.api.controleestoque.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VendasPorFormaPagamentoResponse {
    private String formaPagamento;
    private Long totalVendas;
    private BigDecimal valorTotal;
}

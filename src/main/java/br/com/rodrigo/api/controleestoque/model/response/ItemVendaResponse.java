package br.com.rodrigo.api.controleestoque.model.response;

import br.com.rodrigo.api.controleestoque.model.response.ProdutoResponse;
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
public class ItemVendaResponse {
    private Long id;
    private ProdutoResponse produto;
    private Integer quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;
}

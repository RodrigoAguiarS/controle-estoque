package br.com.rodrigo.api.controleestoque.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProdutoLucroResponse {
    private String descricaoProduto;
    private Long quantidadeSaida;
    private BigDecimal totalLucro;
}

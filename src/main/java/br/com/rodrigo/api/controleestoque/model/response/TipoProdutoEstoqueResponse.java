package br.com.rodrigo.api.controleestoque.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TipoProdutoEstoqueResponse {
    private String tipoProduto;
    private Long quantidadeSaida;
    private Long quantidadeDisponivel;
}

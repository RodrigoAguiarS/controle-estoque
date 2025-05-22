package br.com.rodrigo.api.controleestoque.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TipoProdutoResponse {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal margemLucro;
    private Boolean ativo;
}

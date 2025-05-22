package br.com.rodrigo.api.controleestoque.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoResponse {
    private Long id;
    private String descricao;
    private TipoProdutoResponse tipoProduto;
    private BigDecimal valorFornecedor;
    private Integer quantidadeEstoque;
    private LocalDateTime dataCriacao;
    private Boolean ativo;
}

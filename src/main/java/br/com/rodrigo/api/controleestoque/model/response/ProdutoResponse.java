package br.com.rodrigo.api.controleestoque.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    private BigDecimal valorVenda;
    private Integer quantidadeEstoque;
    private UnidadeResponse unidade;
    private List<String> arquivosUrl;
    private LocalDateTime dataCriacao;
    private Boolean ativo;
}

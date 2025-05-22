package br.com.rodrigo.api.controleestoque.model.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoForm {
    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotNull(message = "O tipo do produto é obrigatório")
    private Long tipoProdutoId;

    @NotNull(message = "O valor do fornecedor é obrigatório")
    @Positive(message = "O valor do fornecedor deve ser positivo")
    private BigDecimal valorFornecedor;

    @NotNull(message = "A quantidade em estoque é obrigatória")
    @PositiveOrZero(message = "A quantidade em estoque deve ser zero ou positiva")
    private Integer quantidadeEstoque;
}

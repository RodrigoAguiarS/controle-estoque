package br.com.rodrigo.api.controleestoque.model.form;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TipoProdutoForm {

    @NotBlank
    @Size(max = 100)
    private String nome;

    @NotBlank
    @Size(max = 500)
    private String descricao;

    @NotNull(message = "A margem de lucro é obrigatória")
    @DecimalMin(value = "0.01", message = "A margem deve ser maior que 0,01%")
    @DecimalMax(value = "100.00", message = "A margem deve ser menor que 100%")
    @Digits(integer = 3, fraction = 2, message = "A margem deve ter no máximo 3 d�gitos antes da vírgula e 2 depois")
    private BigDecimal margemLucro;
}

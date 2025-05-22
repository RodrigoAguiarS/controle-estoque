package br.com.rodrigo.api.controleestoque.model.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormaDePagamentoForm {

    @NotBlank
    @Size(max = 100)
    private String nome;

    @NotBlank
    @Size(max = 500)
    private String descricao;

    @DecimalMin(value = "0.00", inclusive = true, message = "A porcentagem de acrescimo deve ser maior ou igual a 0,00%")
    @DecimalMax(value = "100.00", inclusive = true, message = "A porcentagem de acrescimo deve ser menor ou igual a 100%")
    @Digits(integer = 3, fraction = 2, message = "A porcentagem deve ter no maximo 3 digitos inteiros e 2 decimais")
    private BigDecimal porcentagemAcrescimo;
}

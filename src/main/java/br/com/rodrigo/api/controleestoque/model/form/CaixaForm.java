package br.com.rodrigo.api.controleestoque.model.form;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CaixaForm {

    @NotNull(message = "O valorInicial do caixa é obrigatório")
    @Positive(message = "O valorInicial do caixa deve ser positivo")
    private BigDecimal valorInicial;
}

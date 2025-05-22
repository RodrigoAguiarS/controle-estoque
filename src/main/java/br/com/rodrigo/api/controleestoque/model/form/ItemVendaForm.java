package br.com.rodrigo.api.controleestoque.model.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemVendaForm {
    @NotNull(message = "O ID do produto � obrigat�rio")
    private Long produtoId;

    @NotNull(message = "A quantidade � obrigat�ria")
    @Min(value = 1, message = "A quantidade deve ser maior que zero")
    private Integer quantidade;
}

package br.com.rodrigo.api.controleestoque.model.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}

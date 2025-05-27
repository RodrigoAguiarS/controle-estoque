package br.com.rodrigo.api.controleestoque.model.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnidadeForm {

    @NotBlank(message = "O nome da unidade é obrigatório.")
    private String nome;

    @NotNull(message = "O ID da empresa vinculada é obrigatório.")
    private Long empresa;

    @NotBlank(message = "O telefone da unidade é obrigatório.")
    private String telefone;
}

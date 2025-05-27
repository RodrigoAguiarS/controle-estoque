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
public class EmpresaForm {

    @NotBlank(message = "O nome da empresa é obrigatório.")
    @Size(max = 100, message = "O nome da empresa deve ter no máximo 100 caracteres.")
    private String nome;

    @NotBlank(message = "O CNPJ da empresa é obrigatório.")
    @Size(min = 14, max = 14, message = "O CNPJ deve ter exatamente 14 caracteres.")
    private String cnpj;
}
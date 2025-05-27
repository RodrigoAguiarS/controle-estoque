package br.com.rodrigo.api.controleestoque.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnidadeResponse {

    private Long id;
    private String nome;
    private String telefone;
    private EmpresaResponse empresa;
    private Boolean ativo;
}
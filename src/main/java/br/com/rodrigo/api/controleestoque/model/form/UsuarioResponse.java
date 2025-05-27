package br.com.rodrigo.api.controleestoque.model.form;

import br.com.rodrigo.api.controleestoque.model.response.PerfilResponse;
import br.com.rodrigo.api.controleestoque.model.response.PessoaResponse;
import br.com.rodrigo.api.controleestoque.model.response.UnidadeResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {
    private Long id;
    private String email;
    private PessoaResponse pessoa;
    private UnidadeResponse unidade;
    private Set<PerfilResponse> perfis;
    private Boolean ativo;
}

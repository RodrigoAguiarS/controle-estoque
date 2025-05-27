package br.com.rodrigo.api.controleestoque.conversor;

import br.com.rodrigo.api.controleestoque.model.Pessoa;
import br.com.rodrigo.api.controleestoque.model.response.PessoaResponse;

public class PessoaMapper {

    public static PessoaResponse entidadeParaResponse(Pessoa pessoa) {
        return PessoaResponse.builder()
                .id(pessoa.getId())
                .nome(pessoa.getNome())
                .telefone(pessoa.getTelefone())
                .build();
    }

    public static Pessoa responseParaEntidade(PessoaResponse response) {
        return Pessoa.builder()
                .id(response.getId())
                .nome(response.getNome())
                .telefone(response.getTelefone())
                .build();
    }
}

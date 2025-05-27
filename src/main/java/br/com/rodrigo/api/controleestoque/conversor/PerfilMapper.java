package br.com.rodrigo.api.controleestoque.conversor;

import br.com.rodrigo.api.controleestoque.model.Perfil;
import br.com.rodrigo.api.controleestoque.model.response.PerfilResponse;

public class PerfilMapper {

    public static PerfilResponse entidadeParaResponse(Perfil perfil) {
             return PerfilResponse.builder()
                     .id(perfil.getId())
                     .nome(perfil.getNome())
                     .descricao(perfil.getDescricao())
                     .ativo(perfil.getAtivo())
                     .build();
         }

    public static Perfil responseParaEntidade(PerfilResponse response) {
        return Perfil.builder()
                .id(response.getId())
                .nome(response.getNome())
                .descricao(response.getDescricao())
                .ativo(response.getAtivo())
                .build();
    }
}

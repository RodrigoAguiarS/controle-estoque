package br.com.rodrigo.api.controleestoque.conversor;

import br.com.rodrigo.api.controleestoque.model.Unidade;
import br.com.rodrigo.api.controleestoque.model.response.UnidadeResponse;

public class UnidadeMapper {

    public static UnidadeResponse entidadeParaResponse(Unidade unidade) {
        return UnidadeResponse.builder()
                .id(unidade.getId())
                .nome(unidade.getNome())
                .telefone(unidade.getTelefone())
                .ativo(unidade.getAtivo())
                .build();
    }

    public static Unidade responseParaEntidade(UnidadeResponse response) {
        return Unidade.builder()
                .id(response.getId())
                .nome(response.getNome())
                .telefone(response.getTelefone())
                .empresa(EmpresaMapper.responseParaEntidadeSemUnidade(response.getEmpresa()))
                .ativo(response.getAtivo())
                .build();
    }

    public static Unidade responseParaEntidadeSemEmpresa(UnidadeResponse response) {
        return Unidade.builder()
                .id(response.getId())
                .nome(response.getNome())
                .telefone(response.getTelefone())
                .ativo(response.getAtivo())
                .build();
    }

    public static UnidadeResponse entidadeParaResponseComEmpresa(Unidade unidade) {
        return UnidadeResponse.builder()
                .id(unidade.getId())
                .nome(unidade.getNome())
                .empresa(EmpresaMapper.entidadeResponseSemUnidades(unidade.getEmpresa()))
                .telefone(unidade.getTelefone())
                .ativo(unidade.getAtivo())
                .build();
    }
}

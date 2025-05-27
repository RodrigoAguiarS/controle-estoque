package br.com.rodrigo.api.controleestoque.conversor;

import br.com.rodrigo.api.controleestoque.model.Empresa;
import br.com.rodrigo.api.controleestoque.model.response.EmpresaResponse;

import java.util.Collections;
import java.util.stream.Collectors;

public class EmpresaMapper {

    public static Empresa responseParaEntidade(EmpresaResponse response) {
        return Empresa.builder()
                .id(response.getId())
                .nome(response.getNome())
                .cnpj(response.getCnpj())
                .unidades(response.getUnidades().stream()
                        .map(UnidadeMapper::responseParaEntidadeSemEmpresa)
                        .collect(Collectors.toList()))
                .build();
    }

    public static EmpresaResponse entidadeParaResponse(Empresa empresa) {
        return EmpresaResponse.builder()
                .id(empresa.getId())
                .nome(empresa.getNome())
                .cnpj(empresa.getCnpj())
                .unidades(empresa.getUnidades() != null
                        ? empresa.getUnidades().stream()
                        .map(UnidadeMapper::entidadeParaResponse)
                        .collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }

    public static EmpresaResponse entidadeResponseSemUnidades(Empresa empresa) {
        return EmpresaResponse.builder()
                .id(empresa.getId())
                .nome(empresa.getNome())
                .cnpj(empresa.getCnpj())
                .build();
    }

    public static Empresa responseParaEntidadeSemUnidade(EmpresaResponse response) {
        return Empresa.builder()
                .id(response.getId())
                .nome(response.getNome())
                .cnpj(response.getCnpj())
                .build();
    }
}

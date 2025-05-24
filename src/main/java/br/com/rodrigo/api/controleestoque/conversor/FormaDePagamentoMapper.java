package br.com.rodrigo.api.controleestoque.conversor;

import br.com.rodrigo.api.controleestoque.model.FormaDePagamento;
import br.com.rodrigo.api.controleestoque.model.response.FormaDePagamentoResponse;

public class FormaDePagamentoMapper {

    public static FormaDePagamentoResponse entidadeParaResponse(FormaDePagamento entidade) {
        return FormaDePagamentoResponse.builder()
                .id(entidade.getId())
                .nome(entidade.getNome())
                .descricao(entidade.getDescricao())
                .porcentagemAcrescimo(entidade.getPorcentagemAcrescimo())
                .ativo(entidade.getAtivo())
                .build();
    }

    public static FormaDePagamento responseParaEntidade(FormaDePagamentoResponse response) {
        return FormaDePagamento.builder()
                .id(response.getId())
                .nome(response.getNome())
                .descricao(response.getDescricao())
                .porcentagemAcrescimo(response.getPorcentagemAcrescimo())
                .ativo(response.getAtivo())
                .build();
    }
}
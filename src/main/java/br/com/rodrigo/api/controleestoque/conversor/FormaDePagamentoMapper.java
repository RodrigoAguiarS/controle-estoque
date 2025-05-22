package br.com.rodrigo.api.controleestoque.conversor;

import br.com.rodrigo.api.controleestoque.model.FormaDePagamento;
import br.com.rodrigo.api.controleestoque.model.response.FormaDePagamentoResponse;
import org.springframework.stereotype.Component;

@Component
public class FormaDePagamentoMapper implements IMapper<FormaDePagamento, FormaDePagamentoResponse> {

    @Override
    public FormaDePagamentoResponse entidadeParaResponse(FormaDePagamento entidade) {
        return FormaDePagamentoResponse.builder()
                .id(entidade.getId())
                .nome(entidade.getNome())
                .descricao(entidade.getDescricao())
                .porcentagemAcrescimo(entidade.getPorcentagemAcrescimo())
                .ativo(entidade.getAtivo())
                .build();
    }

    @Override
    public FormaDePagamento responseParaEntidade(FormaDePagamentoResponse response) {
        return FormaDePagamento.builder()
                .id(response.getId())
                .nome(response.getNome())
                .descricao(response.getDescricao())
                .porcentagemAcrescimo(response.getPorcentagemAcrescimo())
                .ativo(response.getAtivo())
                .build();
    }
}

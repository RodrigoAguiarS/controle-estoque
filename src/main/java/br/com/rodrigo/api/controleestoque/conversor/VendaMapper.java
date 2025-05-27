package br.com.rodrigo.api.controleestoque.conversor;

import br.com.rodrigo.api.controleestoque.model.Venda;
import br.com.rodrigo.api.controleestoque.model.response.VendaResponse;

public class VendaMapper {

    public static VendaResponse entidadeParaResponse(Venda venda) {
        return VendaResponse.builder()
                .id(venda.getId())
                .itens(venda.getItens().stream()
                        .map(ItemVendaMapper::entidadeParaResponse)
                        .toList())
                .valorTotal(venda.getValorTotal())
                .subtotal(venda.getSubtotal())
                .formaDePagamento(FormaDePagamentoMapper.entidadeParaResponse(venda.getFormaDePagamento()))
                .observacao(venda.getObservacao())
                .unidade(UnidadeMapper.entidadeParaResponse(venda.getUnidade()))
                .criadoEm(venda.getCriadoEm())
                .ativo(venda.getAtivo())
                .build();
    }

    public static Venda responseParaEntidade(VendaResponse response) {
        return Venda.builder()
                .id(response.getId())
                .itens(response.getItens().stream()
                        .map(ItemVendaMapper::responseParaEntidade)
                        .toList())
                .formaDePagamento(FormaDePagamentoMapper.responseParaEntidade(response.getFormaDePagamento()))
                .valorTotal(response.getValorTotal())
                .subtotal(response.getSubtotal())
                .unidade(UnidadeMapper.responseParaEntidade(response.getUnidade()))
                .observacao(response.getObservacao())
                .build();
    }
}

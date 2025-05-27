package br.com.rodrigo.api.controleestoque.conversor;

import br.com.rodrigo.api.controleestoque.model.ItemVenda;
import br.com.rodrigo.api.controleestoque.model.response.ItemVendaResponse;

public class ItemVendaMapper {

    public static ItemVendaResponse entidadeParaResponse(ItemVenda itemVenda) {
        return ItemVendaResponse.builder()
                .id(itemVenda.getId())
                .produto(ProdutoMapper.entidadeParaResponse(itemVenda.getProduto()))
                .quantidade(itemVenda.getQuantidade())
                .unidade(UnidadeMapper.entidadeParaResponseComEmpresa(itemVenda.getUnidade()))
                .valorUnitario(itemVenda.getValorUnitario())
                .valorTotal(itemVenda.getValorTotal())
                .build();
    }

    public static ItemVenda responseParaEntidade(ItemVendaResponse response) {
        return ItemVenda.builder()
                .id(response.getId())
                .quantidade(response.getQuantidade())
                .unidade(UnidadeMapper.responseParaEntidade(response.getUnidade()))
                .produto(ProdutoMapper.responseParaEntidade(response.getProduto()))
                .valorUnitario(response.getValorUnitario())
                .valorTotal(response.getValorTotal())
                .build();
    }
}

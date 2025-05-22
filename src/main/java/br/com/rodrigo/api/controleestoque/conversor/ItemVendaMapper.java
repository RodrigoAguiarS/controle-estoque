package br.com.rodrigo.api.controleestoque.conversor;

import br.com.rodrigo.api.controleestoque.model.ItemVenda;
import br.com.rodrigo.api.controleestoque.model.form.ItemVendaForm;
import br.com.rodrigo.api.controleestoque.model.response.ItemVendaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemVendaMapper implements IMapper<ItemVenda, ItemVendaForm, ItemVendaResponse> {

    private final ProdutoMapper produtoMapper;

    @Override
    public ItemVendaResponse entidadeParaResponse(ItemVenda itemVenda) {
        return ItemVendaResponse.builder()
                .id(itemVenda.getId())
                .produto(produtoMapper.entidadeParaResponse(itemVenda.getProduto()))
                .quantidade(itemVenda.getQuantidade())
                .valorUnitario(itemVenda.getValorUnitario())
                .valorTotal(itemVenda.getValorTotal())
                .build();
    }

    @Override
    public ItemVenda formParaEntidade(ItemVendaForm form) {
        return ItemVenda.builder()
                .quantidade(form.getQuantidade())
                .build();
    }

    @Override
    public ItemVenda responseParaEntidade(ItemVendaResponse response) {
        return ItemVenda.builder()
                .id(response.getId())
                .quantidade(response.getQuantidade())
                .valorUnitario(response.getValorUnitario())
                .valorTotal(response.getValorTotal())
                .build();
    }
}

package br.com.rodrigo.api.controleestoque.conversor;

import br.com.rodrigo.api.controleestoque.model.Venda;
import br.com.rodrigo.api.controleestoque.model.form.VendaForm;
import br.com.rodrigo.api.controleestoque.model.response.VendaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VendaMapper implements IMapper<Venda, VendaForm, VendaResponse> {

    private final ItemVendaMapper itemVendaMapper;

    @Override
    public VendaResponse entidadeParaResponse(Venda venda) {
        return VendaResponse.builder()
                .id(venda.getId())
                .itens(venda.getItens().stream()
                        .map(itemVendaMapper::entidadeParaResponse)
                        .toList())
                .valorTotal(venda.getValorTotal())
                .observacao(venda.getObservacao())
                .criadoEm(venda.getCriadoEm())
                .ativo(venda.getAtivo())
                .build();
    }

    @Override
    public Venda responseParaEntidade(VendaResponse response) {
        return Venda.builder()
                .id(response.getId())
                .itens(response.getItens().stream()
                        .map(itemVendaMapper::responseParaEntidade)
                        .toList())
                .valorTotal(response.getValorTotal())
                .observacao(response.getObservacao())
                .build();
    }
}

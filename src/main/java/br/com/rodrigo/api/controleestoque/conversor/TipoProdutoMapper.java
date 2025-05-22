package br.com.rodrigo.api.controleestoque.conversor;

import br.com.rodrigo.api.controleestoque.model.TipoProduto;
import br.com.rodrigo.api.controleestoque.model.response.TipoProdutoResponse;
import org.springframework.stereotype.Component;

@Component
public class TipoProdutoMapper implements IMapper<TipoProduto, TipoProdutoResponse> {

    @Override
    public TipoProdutoResponse entidadeParaResponse(TipoProduto entidade) {
        return TipoProdutoResponse.builder()
                .id(entidade.getId())
                .nome(entidade.getNome())
                .margemLucro(entidade.getMargemLucro())
                .descricao(entidade.getDescricao())
                .ativo(entidade.getAtivo())
                .build();
    }

    @Override
    public TipoProduto responseParaEntidade(TipoProdutoResponse response) {
        return TipoProduto.builder()
                .id(response.getId())
                .nome(response.getNome())
                .margemLucro(response.getMargemLucro())
                .descricao(response.getDescricao())
                .ativo(response.getAtivo())
                .build();
    }
}

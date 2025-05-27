package br.com.rodrigo.api.controleestoque.conversor;

import br.com.rodrigo.api.controleestoque.model.Produto;
import br.com.rodrigo.api.controleestoque.model.response.ProdutoResponse;

public class ProdutoMapper {

    public static ProdutoResponse entidadeParaResponse(Produto entidade) {
        return ProdutoResponse.builder()
                .id(entidade.getId())
                .descricao(entidade.getDescricao())
                .tipoProduto(TipoProdutoMapper.entidadeParaResponse(entidade.getTipoProduto()))
                .valorFornecedor(entidade.getValorFornecedor())
                .valorVenda(entidade.getValorVenda())
                .unidade(UnidadeMapper.entidadeParaResponse(entidade.getTipoProduto().getUnidade()))
                .quantidadeEstoque(entidade.getQuantidadeEstoque())
                .arquivosUrl(entidade.getArquivosUrl())
                .dataCriacao(entidade.getCriadoEm())
                .ativo(entidade.getAtivo())
                .build();
    }

    public static Produto responseParaEntidade(ProdutoResponse response) {
        return Produto.builder()
                .id(response.getId())
                .descricao(response.getDescricao())
                .valorFornecedor(response.getValorFornecedor())
                .valorVenda(response.getValorVenda())
                .unidade(UnidadeMapper.responseParaEntidade(response.getUnidade()))
                .tipoProduto(TipoProdutoMapper.responseParaEntidade(response.getTipoProduto()))
                .arquivosUrl(response.getArquivosUrl())
                .quantidadeEstoque(response.getQuantidadeEstoque())
                .build();
    }
}

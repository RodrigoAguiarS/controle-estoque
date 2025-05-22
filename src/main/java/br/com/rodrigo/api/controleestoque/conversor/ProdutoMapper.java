package br.com.rodrigo.api.controleestoque.conversor;

import br.com.rodrigo.api.controleestoque.model.Produto;
import br.com.rodrigo.api.controleestoque.model.form.ProdutoForm;
import br.com.rodrigo.api.controleestoque.model.response.ProdutoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProdutoMapper implements IMapper<Produto, ProdutoForm, ProdutoResponse> {

    private final TipoProdutoMapper tipoProdutoMapper;

    @Override
    public ProdutoResponse entidadeParaResponse(Produto entidade) {
        return ProdutoResponse.builder()
                .id(entidade.getId())
                .descricao(entidade.getDescricao())
                .tipoProduto(tipoProdutoMapper.entidadeParaResponse(entidade.getTipoProduto()))
                .valorFornecedor(entidade.getValorFornecedor())
                .quantidadeEstoque(entidade.getQuantidadeEstoque())
                .dataCriacao(entidade.getCriadoEm())
                .ativo(entidade.getAtivo())
                .build();
    }

    @Override
    public Produto formParaEntidade(ProdutoForm form) {
        return Produto.builder()
                .descricao(form.getDescricao())
                .valorFornecedor(form.getValorFornecedor())
                .quantidadeEstoque(form.getQuantidadeEstoque())
                .build();
    }

    @Override
    public Produto responseParaEntidade(ProdutoResponse response) {
        return Produto.builder()
                .id(response.getId())
                .descricao(response.getDescricao())
                .valorFornecedor(response.getValorFornecedor())
                .quantidadeEstoque(response.getQuantidadeEstoque())
                .build();
    }
}

package br.com.rodrigo.api.controleestoque.service.strategy;

import br.com.rodrigo.api.controleestoque.model.MovimentacaoEstoque;
import br.com.rodrigo.api.controleestoque.model.Produto;
import br.com.rodrigo.api.controleestoque.model.TipoMovimentacao;
import br.com.rodrigo.api.controleestoque.model.TipoOperacao;
import br.com.rodrigo.api.controleestoque.repository.MovimentacaoEstoqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CadastroProdutoStrategy implements MovimentacaoEstoqueStrategy {

    private final MovimentacaoEstoqueRepository movimentacaoRepository;

    @Override
    public void executar(Produto produto, int quantidade, TipoOperacao operacao, BigDecimal valor) {
        MovimentacaoEstoque movimentacao = MovimentacaoEstoque.builder()
                .produto(produto)
                .tipo(TipoMovimentacao.ENTRADA)
                .operacao(operacao)
                .unidade(produto.getUnidade())
                .quantidade(quantidade)
                .valorMovimentacao(valor)
                .build();

        int novoEstoque = produto.getQuantidadeEstoque() != null ?
                produto.getQuantidadeEstoque() + quantidade : quantidade;
        produto.setQuantidadeEstoque(novoEstoque);

        movimentacaoRepository.save(movimentacao);
    }
}
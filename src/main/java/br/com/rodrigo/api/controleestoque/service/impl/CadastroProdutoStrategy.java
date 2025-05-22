package br.com.rodrigo.api.controleestoque.service.impl;

import br.com.rodrigo.api.controleestoque.model.MovimentacaoEstoque;
import br.com.rodrigo.api.controleestoque.model.Produto;
import br.com.rodrigo.api.controleestoque.model.TipoMovimentacao;
import br.com.rodrigo.api.controleestoque.model.TipoOperacao;
import br.com.rodrigo.api.controleestoque.repository.MovimentacaoEstoqueRepository;
import br.com.rodrigo.api.controleestoque.repository.ProdutoRepository;
import br.com.rodrigo.api.controleestoque.service.MovimentacaoEstoqueStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CadastroProdutoStrategy implements MovimentacaoEstoqueStrategy {

    private final MovimentacaoEstoqueRepository movimentacaoRepository;
    private final ProdutoRepository produtoRepository;

    @Override
    public void executar(Produto produto, int quantidade, TipoOperacao operacao, BigDecimal valor) {
        MovimentacaoEstoque movimentacao = MovimentacaoEstoque.builder()
                .produto(produto)
                .tipo(TipoMovimentacao.ENTRADA)
                .operacao(operacao)
                .quantidade(quantidade)
                .valorMovimentacao(valor)
                .build();

        int novoEstoque = produto.getQuantidadeEstoque() != null ?
                produto.getQuantidadeEstoque() + quantidade : quantidade;
        produto.setQuantidadeEstoque(novoEstoque);

        produtoRepository.save(produto);
        movimentacaoRepository.save(movimentacao);
    }
}
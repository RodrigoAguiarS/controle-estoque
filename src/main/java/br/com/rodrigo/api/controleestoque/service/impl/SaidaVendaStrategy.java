package br.com.rodrigo.api.controleestoque.service.impl;

import br.com.rodrigo.api.controleestoque.exception.MensagensError;
import br.com.rodrigo.api.controleestoque.exception.ViolacaoIntegridadeDadosException;
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
public class SaidaVendaStrategy implements MovimentacaoEstoqueStrategy {
    private final MovimentacaoEstoqueRepository movimentacaoRepository;
    private final ProdutoRepository produtoRepository;

    @Override
    public void executar(Produto produto, int quantidade, TipoOperacao operacao, BigDecimal valor) {
        if (produto.getQuantidadeEstoque() < quantidade) {
            throw new ViolacaoIntegridadeDadosException(MensagensError.ESTOQUE_INSUFICIENTE.getMessage(produto.getDescricao(), produto.getQuantidadeEstoque()));
        }

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidade);

        MovimentacaoEstoque movimentacao = MovimentacaoEstoque.builder()
                .produto(produto)
                .tipo(TipoMovimentacao.SAIDA)
                .operacao(operacao)
                .quantidade(quantidade)
                .valorMovimentacao(valor)
                .build();

        produtoRepository.save(produto);
        movimentacaoRepository.save(movimentacao);
    }
}

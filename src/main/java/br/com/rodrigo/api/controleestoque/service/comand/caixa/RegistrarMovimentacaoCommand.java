package br.com.rodrigo.api.controleestoque.service.comand.caixa;


import br.com.rodrigo.api.controleestoque.exception.MensagensError;
import br.com.rodrigo.api.controleestoque.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.controleestoque.exception.ViolacaoIntegridadeDadosException;
import br.com.rodrigo.api.controleestoque.model.Caixa;
import br.com.rodrigo.api.controleestoque.model.MovimentacaoCaixa;
import br.com.rodrigo.api.controleestoque.model.TipoMovimentacao;
import br.com.rodrigo.api.controleestoque.repository.CaixaRepository;
import br.com.rodrigo.api.controleestoque.repository.MovimentacaoCaixaRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class RegistrarMovimentacaoCommand implements CaixaCommand {

    private final CaixaRepository caixaRepository;
    private final MovimentacaoCaixaRepository movimentacaoRepository;
    private final Long caixaId;
    private final BigDecimal valor;
    private final TipoMovimentacao tipo;
    private final String descricao;

    @Override
    public void executar() {
        Caixa caixa = caixaRepository.findById(caixaId)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.CAIXA_NAO_ENCONTRADO.getMessage()));

        if (tipo == TipoMovimentacao.SAIDA && caixa.getValorAtual().compareTo(valor) < 0) {
            throw new ViolacaoIntegridadeDadosException(
                    MensagensError.CAIXA_SALDO_INSUFICIENTE.getMessage(caixa.getId(), caixa.getValorAtual(), valor));
        }

        MovimentacaoCaixa movimentacao = MovimentacaoCaixa.builder()
                .caixa(caixa)
                .valor(valor)
                .tipo(tipo)
                .unidade(caixa.getUnidade())
                .descricao(descricao)
                .build();

        movimentacaoRepository.save(movimentacao);

        BigDecimal novoValorAtual = tipo == TipoMovimentacao.ENTRADA
                ? caixa.getValorAtual().add(valor)
                : caixa.getValorAtual().subtract(valor);

        caixa.setValorAtual(novoValorAtual);
        caixaRepository.save(caixa);
    }
}

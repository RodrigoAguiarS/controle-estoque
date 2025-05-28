package br.com.rodrigo.api.controleestoque.service.comand.venda;

import br.com.rodrigo.api.controleestoque.exception.MensagensError;
import br.com.rodrigo.api.controleestoque.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.controleestoque.model.ItemVenda;
import br.com.rodrigo.api.controleestoque.model.TipoMovimentacao;
import br.com.rodrigo.api.controleestoque.model.TipoOperacao;
import br.com.rodrigo.api.controleestoque.model.Venda;
import br.com.rodrigo.api.controleestoque.repository.CaixaRepository;
import br.com.rodrigo.api.controleestoque.repository.MovimentacaoCaixaRepository;
import br.com.rodrigo.api.controleestoque.repository.VendaRepository;
import br.com.rodrigo.api.controleestoque.service.comand.caixa.RegistrarMovimentacaoCommand;
import br.com.rodrigo.api.controleestoque.service.strategy.MovimentacaoEstoqueService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class CancelarVendaCommand implements VendaCommand {

    private final Long vendaId;
    private final VendaRepository vendaRepository;
    private final MovimentacaoEstoqueService movimentacaoService;
    private final CaixaRepository caixaRepository;
    private final MovimentacaoCaixaRepository movimentacaoRepository;

    @Override
    public void executar() {
        Venda venda = vendaRepository.findById(vendaId)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.VENDA_NAO_ENCONTRADA.getMessage(vendaId)));

        venda.getItens().stream()
                .filter(item -> item.getQuantidade() > 0)
                .forEach(item -> processarItemVenda(item, venda));

        venda.desativar();
        vendaRepository.save(venda);
    }

    private void processarItemVenda(ItemVenda item, Venda venda) {
        movimentacaoService.processarMovimentacao(
                item.getProduto(),
                TipoMovimentacao.ENTRADA,
                TipoOperacao.CANCELAMENTO_VENDA,
                item.getQuantidade(),
                item.getValorTotal()
        );

        BigDecimal acrescimoPercentual = venda.getFormaDePagamento().getPorcentagemAcrescimo();
        BigDecimal acrescimoDecimal = acrescimoPercentual.divide(BigDecimal.valueOf(100));
        BigDecimal valorComAcrescimo = item.getValorTotal().add(item.getValorTotal().multiply(acrescimoDecimal));

        RegistrarMovimentacaoCommand command = new RegistrarMovimentacaoCommand(
                caixaRepository,
                movimentacaoRepository,
                venda.getCaixa().getId(),
                valorComAcrescimo,
                TipoMovimentacao.SAIDA,
                "Cancelamento da venda ID: " + venda.getId()
        );
        command.executar();
    }
}

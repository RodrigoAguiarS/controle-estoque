package br.com.rodrigo.api.controleestoque.service.comand.caixa;

import br.com.rodrigo.api.controleestoque.exception.MensagensError;
import br.com.rodrigo.api.controleestoque.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.controleestoque.model.Caixa;
import br.com.rodrigo.api.controleestoque.model.MovimentacaoCaixa;
import br.com.rodrigo.api.controleestoque.model.TipoMovimentacao;
import br.com.rodrigo.api.controleestoque.model.Venda;
import br.com.rodrigo.api.controleestoque.repository.CaixaRepository;
import br.com.rodrigo.api.controleestoque.repository.MovimentacaoCaixaRepository;
import br.com.rodrigo.api.controleestoque.repository.VendaRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegistrarVendaCommand implements CaixaCommand {

    private final CaixaRepository caixaRepository;
    private final VendaRepository vendaRepository;
    private final MovimentacaoCaixaRepository movimentacaoCaixaRepository;
    private final Long caixaId;
    private final Venda venda;

    @Override
    public void executar() {
        Caixa caixa = caixaRepository.findById(caixaId)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.CAIXA_NAO_ENCONTRADO.getMessage()));

        venda.setCaixa(caixa);
        vendaRepository.save(venda);

        MovimentacaoCaixa movimentacao = MovimentacaoCaixa.builder()
                .caixa(caixa)
                .valor(venda.getValorTotal())
                .tipo(TipoMovimentacao.ENTRADA)
                .unidade(caixa.getUnidade())
                .descricao("Venda registrada: " + venda.getId())
                .build();
        movimentacaoCaixaRepository.save(movimentacao);

        caixa.setValorAtual(caixa.getValorAtual().add(venda.getValorTotal()));
        caixaRepository.save(caixa);
    }
}
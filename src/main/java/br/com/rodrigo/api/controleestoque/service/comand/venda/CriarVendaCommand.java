package br.com.rodrigo.api.controleestoque.service.comand.venda;

import br.com.rodrigo.api.controleestoque.conversor.FormaDePagamentoMapper;
import br.com.rodrigo.api.controleestoque.exception.MensagensError;
import br.com.rodrigo.api.controleestoque.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.controleestoque.model.Caixa;
import br.com.rodrigo.api.controleestoque.model.ItemVenda;
import br.com.rodrigo.api.controleestoque.model.Produto;
import br.com.rodrigo.api.controleestoque.model.TipoMovimentacao;
import br.com.rodrigo.api.controleestoque.model.TipoOperacao;
import br.com.rodrigo.api.controleestoque.model.Venda;
import br.com.rodrigo.api.controleestoque.model.form.ItemVendaForm;
import br.com.rodrigo.api.controleestoque.model.form.VendaForm;
import br.com.rodrigo.api.controleestoque.model.response.FormaDePagamentoResponse;
import br.com.rodrigo.api.controleestoque.repository.CaixaRepository;
import br.com.rodrigo.api.controleestoque.repository.ProdutoRepository;
import br.com.rodrigo.api.controleestoque.repository.VendaRepository;
import br.com.rodrigo.api.controleestoque.service.IFormaDePagamento;
import br.com.rodrigo.api.controleestoque.service.calculo.CalculoValorTotalComAcrescimoStrategy;
import br.com.rodrigo.api.controleestoque.service.calculo.CalculoValorTotalStrategy;
import br.com.rodrigo.api.controleestoque.service.comand.caixa.CaixaCommandExecutor;
import br.com.rodrigo.api.controleestoque.service.comand.caixa.RegistrarVendaCommand;
import br.com.rodrigo.api.controleestoque.service.singleton.UsuarioContext;
import br.com.rodrigo.api.controleestoque.service.strategy.MovimentacaoEstoqueService;
import br.com.rodrigo.api.controleestoque.util.CaixaUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CriarVendaCommand implements VendaCommand {

    private final VendaForm vendaForm;
    private final ProdutoRepository produtoRepository;
    private final IFormaDePagamento formaDePagamentoService;
    private final MovimentacaoEstoqueService movimentacaoService;
    private final CalculoValorTotalStrategy calculoValorTotalStrategy;
    private final CaixaUtil caixaUtil;
    private final CaixaRepository caixaRepository;
    private final CaixaCommandExecutor caixaCommandExecutor;
    private final CalculoValorTotalComAcrescimoStrategy calculoValorTotalComAcrescimoStrategy;
    private final VendaRepository vendaRepository;

    @Getter
    private Venda venda;

    @Override
    public void executar() {

        Caixa caixa = caixaUtil.getCaixaDoUsuarioLogado().orElseThrow(() -> new ObjetoNaoEncontradoException(
                MensagensError.CAIXA_NAO_ENCONTRADO.getMessage(UsuarioContext.getUsuarioLogado().getId())));
        caixaUtil.validarCaixaAtivo(caixa);

        Set<Long> produtoIds = vendaForm.getItens().stream()
                .map(item -> item.getProduto().getId())
                .collect(Collectors.toSet());

        Map<Long, Produto> produtoMap = produtoRepository.findAllById(produtoIds).stream()
                .collect(Collectors.toMap(Produto::getId, produto -> produto));

        FormaDePagamentoResponse formaDePagamento = formaDePagamentoService
                .consultarPorId(vendaForm.getFormaDePagamento())
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.FORMA_PAGAMENTO_NAO_ENCONTRADA.getMessage(vendaForm.getFormaDePagamento())));

        venda = new Venda();
        venda.setUnidade(UsuarioContext.getUsuarioLogado().getUnidade());

        List<ItemVenda> itens = vendaForm.getItens().stream()
                .map(itemForm -> criarItemVenda(itemForm, produtoMap, venda))
                .peek(item -> movimentacaoService.processarMovimentacao(
                        item.getProduto(),
                        TipoMovimentacao.SAIDA,
                        TipoOperacao.VENDA,
                        item.getQuantidade(),
                        item.getValorTotal()
                ))
                .collect(Collectors.toList());

        venda.setItens(itens);
        venda.setFormaDePagamento(FormaDePagamentoMapper.responseParaEntidade(formaDePagamento));

        BigDecimal subTotal = calculoValorTotalStrategy.calcular(venda);
        BigDecimal valorTotalComAcrescimo = calculoValorTotalComAcrescimoStrategy.calcular(venda);

        venda.setValorTotal(valorTotalComAcrescimo);
        venda.setSubtotal(subTotal);
        venda.setCaixa(caixa);
        venda.setObservacao(vendaForm.getObservacao());

        RegistrarVendaCommand registrarVendaCommand = new RegistrarVendaCommand(
                caixaRepository,
                vendaRepository,
                caixa.getId(),
                venda
        );

        caixaCommandExecutor.executar(registrarVendaCommand);

        vendaRepository.save(venda);
    }

    private ItemVenda criarItemVenda(ItemVendaForm itemForm, Map<Long, Produto> produtoMap, Venda venda) {
        Produto produto = produtoMap.get(itemForm.getProduto().getId());
        if (produto == null) {
            throw new ObjetoNaoEncontradoException(
                    MensagensError.PRODUTO_NAO_ENCONTRADO.getMessage(itemForm.getProduto().getId()));
        }

        return ItemVenda.builder()
                .venda(venda)
                .produto(produto)
                .unidade(UsuarioContext.getUsuarioLogado().getUnidade())
                .quantidade(itemForm.getQuantidade())
                .valorUnitario(produto.getValorVenda())
                .valorTotal(produto.getValorVenda()
                        .multiply(BigDecimal.valueOf(itemForm.getQuantidade())))
                .build();
    }
}

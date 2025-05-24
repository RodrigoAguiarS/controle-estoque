package br.com.rodrigo.api.controleestoque.service.impl;

import br.com.rodrigo.api.controleestoque.conversor.FormaDePagamentoMapper;
import br.com.rodrigo.api.controleestoque.conversor.VendaMapper;
import br.com.rodrigo.api.controleestoque.exception.MensagensError;
import br.com.rodrigo.api.controleestoque.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.controleestoque.model.ItemVenda;
import br.com.rodrigo.api.controleestoque.model.Produto;
import br.com.rodrigo.api.controleestoque.model.TipoMovimentacao;
import br.com.rodrigo.api.controleestoque.model.TipoOperacao;
import br.com.rodrigo.api.controleestoque.model.Venda;
import br.com.rodrigo.api.controleestoque.model.form.ItemVendaForm;
import br.com.rodrigo.api.controleestoque.model.form.VendaForm;
import br.com.rodrigo.api.controleestoque.model.response.FormaDePagamentoResponse;
import br.com.rodrigo.api.controleestoque.model.response.VendaResponse;
import br.com.rodrigo.api.controleestoque.repository.ProdutoRepository;
import br.com.rodrigo.api.controleestoque.repository.VendaRepository;
import br.com.rodrigo.api.controleestoque.service.IFormaDePagamento;
import br.com.rodrigo.api.controleestoque.service.IVenda;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendaServiceImpl implements IVenda {

    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;
    private final IFormaDePagamento formaDePagamentoService;
    private final MovimentacaoEstoqueService movimentacaoService;

    @Override
    @Transactional
    public VendaResponse realizarVenda(VendaForm vendaForm) {
        Venda venda = criarVenda(vendaForm);
        venda = vendaRepository.save(venda);
        return construirDto(venda);
    }

    @Override
    public Optional<VendaResponse> buscarPorId(Long id) {
        return vendaRepository.findById(id)
                .map(this::construirDto);
    }

    @Override
    public Page<VendaResponse> listarTodos(int page, int size, String sort, Long id, BigDecimal valorMinimo, BigDecimal valorMaximo, LocalDateTime dataInicio, LocalDateTime dataFim, Long formaDePagamentoId, Boolean ativo) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Venda> vendas = vendaRepository.findAll(id, valorMinimo, valorMaximo, dataInicio, dataFim, ativo, formaDePagamentoId, pageable);
        return vendas.map(VendaMapper::entidadeParaResponse);
    }


    @Override
    @Transactional
    public void cancelarVenda(Long id) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.VENDA_NAO_ENCONTRADA.getMessage(id)));

        venda.getItens().stream()
                .filter(item -> item.getQuantidade() > 0)
                .forEach(item -> movimentacaoService.processarMovimentacao(
                        item.getProduto(),
                        TipoMovimentacao.ENTRADA,
                        TipoOperacao.CANCELAMENTO_VENDA,
                        item.getQuantidade(),
                        item.getValorTotal()
                ));

        venda.desativar();
        vendaRepository.save(venda);
    }

    private Venda criarVenda(VendaForm vendaForm) {
        Set<Long> produtoIds = vendaForm.getItens().stream()
                .map(item -> item.getProduto().getId())
                .collect(Collectors.toSet());

        Map<Long, Produto> produtoMap = produtoRepository.findAllById(produtoIds).stream()
                .collect(Collectors.toMap(Produto::getId, produto -> produto));

        FormaDePagamentoResponse formaDePagamento = formaDePagamentoService
                .consultarPorId(vendaForm.getFormaDePagamento())
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.FORMA_PAGAMENTO_NAO_ENCONTRADA.getMessage(vendaForm.getFormaDePagamento())));

        Venda venda = new Venda();

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

        BigDecimal valorTotal = venda.getItens().stream()
                .map(ItemVenda::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal acrescimo = venda.getFormaDePagamento().getPorcentagemAcrescimo()
                .divide(new BigDecimal("100.00"), 4, RoundingMode.HALF_UP);
        BigDecimal valorAcrescimo = valorTotal.multiply(acrescimo);
        BigDecimal valorTotalComAcrescimo = valorTotal.add(valorAcrescimo)
                .setScale(2, RoundingMode.HALF_UP);

        venda.setValorTotal(valorTotalComAcrescimo);
        venda.setObservacao(vendaForm.getObservacao());

        return venda;
    }

    private ItemVenda criarItemVenda(ItemVendaForm itemForm, Map<Long, Produto> produtoMap, Venda venda) {
        Produto produto = Optional.ofNullable(produtoMap.get(itemForm.getProduto().getId()))
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.PRODUTO_NAO_ENCONTRADO.getMessage(itemForm.getProduto().getId())));

        return ItemVenda.builder()
                .venda(venda)
                .produto(produto)
                .quantidade(itemForm.getQuantidade())
                .valorUnitario(produto.getValorVenda())
                .valorTotal(produto.getValorVenda()
                        .multiply(BigDecimal.valueOf(itemForm.getQuantidade())))
                .build();
    }

    private VendaResponse construirDto(Venda venda) {
        return VendaMapper.entidadeParaResponse(venda);
    }
}

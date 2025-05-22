package br.com.rodrigo.api.controleestoque.service.impl;

import br.com.rodrigo.api.controleestoque.conversor.VendaMapper;
import br.com.rodrigo.api.controleestoque.exception.MensagensError;
import br.com.rodrigo.api.controleestoque.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.controleestoque.model.ItemVenda;
import br.com.rodrigo.api.controleestoque.model.Produto;
import br.com.rodrigo.api.controleestoque.model.Venda;
import br.com.rodrigo.api.controleestoque.model.form.ItemVendaForm;
import br.com.rodrigo.api.controleestoque.model.form.VendaForm;
import br.com.rodrigo.api.controleestoque.model.response.VendaResponse;
import br.com.rodrigo.api.controleestoque.repository.ProdutoRepository;
import br.com.rodrigo.api.controleestoque.repository.VendaRepository;
import br.com.rodrigo.api.controleestoque.service.IVenda;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendaServiceImpl implements IVenda {

    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;
    private final VendaMapper vendaMapper;

    @Override
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
    public Page<VendaResponse> listarTodos(int page, int size, String sort, Long id, BigDecimal valorMinimo,
                                           BigDecimal valorMaximo, LocalDateTime dataInicio, LocalDateTime dataFim,
                                           Boolean ativo) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Venda> vendas = vendaRepository.findAll(id, valorMinimo, valorMaximo, dataInicio, dataFim, ativo, pageable);
        return vendas.map(vendaMapper::entidadeParaResponse);
    }

    @Override
    public BigDecimal calcularLucroVenda(Long vendaId) {
        return null;
    }

    @Override
    public boolean validarEstoque(VendaForm vendaForm) {
        return false;
    }

    @Override
    public void cancelarVenda(Long id) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.VENDA_NAO_ENCONTRADA.getMessage(id)));
        venda.desativar();
        vendaRepository.save(venda);
    }

    private Venda criarVenda(VendaForm vendaForm) {
        Set<Long> produtoIds = vendaForm.getItens().stream()
                .map(ItemVendaForm::getProdutoId)
                .collect(Collectors.toSet());

        Map<Long, Produto> produtoMap = produtoRepository.findAllById(produtoIds).stream()
                .collect(Collectors.toMap(Produto::getId, produto -> produto));

        Venda venda = new Venda();

        venda.setItens(vendaForm.getItens().stream()
                .map(itemForm -> {
                    ItemVenda itemVenda = new ItemVenda();
                    itemVenda.setVenda(venda);

                    Produto produto = produtoMap.get(itemForm.getProdutoId());
                    if (produto == null) {
                        throw new ObjetoNaoEncontradoException(
                                MensagensError.PRODUTO_NAO_ENCONTRADO.getMessage(itemForm.getProdutoId()));
                    }

                    itemVenda.setProduto(produto);
                    itemVenda.setQuantidade(itemForm.getQuantidade());
                    itemVenda.setValorUnitario(produto.getValorFornecedor());
                    itemVenda.setValorTotal(itemVenda.getValorUnitario()
                            .multiply(BigDecimal.valueOf(itemVenda.getQuantidade())));
                    return itemVenda;
                })
                .collect(Collectors.toList()));

        venda.setValorTotal(venda.getItens().stream()
                .map(ItemVenda::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        venda.setObservacao(vendaForm.getObservacao());

        return venda;
    }

    private VendaResponse construirDto(Venda venda) {
        return vendaMapper.entidadeParaResponse(venda);
    }
}

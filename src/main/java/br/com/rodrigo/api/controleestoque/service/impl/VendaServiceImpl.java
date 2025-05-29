package br.com.rodrigo.api.controleestoque.service.impl;

import br.com.rodrigo.api.controleestoque.conversor.VendaMapper;
import br.com.rodrigo.api.controleestoque.model.Venda;
import br.com.rodrigo.api.controleestoque.model.form.VendaForm;
import br.com.rodrigo.api.controleestoque.model.response.VendaResponse;
import br.com.rodrigo.api.controleestoque.repository.CaixaRepository;
import br.com.rodrigo.api.controleestoque.repository.MovimentacaoCaixaRepository;
import br.com.rodrigo.api.controleestoque.repository.ProdutoRepository;
import br.com.rodrigo.api.controleestoque.repository.VendaRepository;
import br.com.rodrigo.api.controleestoque.service.IFormaDePagamento;
import br.com.rodrigo.api.controleestoque.service.IVenda;
import br.com.rodrigo.api.controleestoque.service.calculo.CalculoValorTotalComAcrescimoStrategy;
import br.com.rodrigo.api.controleestoque.service.calculo.CalculoValorTotalStrategy;
import br.com.rodrigo.api.controleestoque.service.comand.caixa.CaixaCommandExecutor;
import br.com.rodrigo.api.controleestoque.service.comand.venda.CancelarVendaCommand;
import br.com.rodrigo.api.controleestoque.service.comand.venda.CriarVendaCommand;
import br.com.rodrigo.api.controleestoque.service.comand.venda.VendaCommandExecutor;
import br.com.rodrigo.api.controleestoque.service.strategy.MovimentacaoEstoqueService;
import br.com.rodrigo.api.controleestoque.util.CaixaUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static br.com.rodrigo.api.controleestoque.service.singleton.UsuarioContext.getUsuarioLogado;

@Service
@RequiredArgsConstructor
public class VendaServiceImpl implements IVenda {

    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;
    private final IFormaDePagamento formaDePagamentoService;
    private final MovimentacaoEstoqueService movimentacaoService;
    private final MovimentacaoCaixaRepository movimentacaoRepository;
    private final VendaCommandExecutor vendaCommandExecutor;
    private final CaixaCommandExecutor caixaCommandExecutor;
    private final CalculoValorTotalStrategy calculoValorTotalStrategy;
    private final CalculoValorTotalComAcrescimoStrategy calculoValorTotalComAcrescimoStrategy;
    private final CaixaRepository caixaRepository;
    private final CaixaUtil caixaUtil;

    @Override
    @Transactional
    public VendaResponse realizarVenda(VendaForm vendaForm) {

        CriarVendaCommand criarVendaCommand = new CriarVendaCommand(
                vendaForm,
                produtoRepository,
                formaDePagamentoService,
                movimentacaoService,
                movimentacaoRepository,
                calculoValorTotalStrategy,
                caixaUtil,
                caixaRepository,
                caixaCommandExecutor,
                calculoValorTotalComAcrescimoStrategy,
                vendaRepository
        );
        vendaCommandExecutor.executar(criarVendaCommand);

        Venda venda = criarVendaCommand.getVenda();
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
        Page<Venda> vendas = vendaRepository.findAll(getUsuarioLogado().getUnidade().getId(), id, valorMinimo, valorMaximo, dataInicio, dataFim, ativo, formaDePagamentoId, pageable);
        return vendas.map(VendaMapper::entidadeParaResponse);
    }

    @Override
    @Transactional
    public void cancelarVenda(Long id) {
        CancelarVendaCommand command = new CancelarVendaCommand(
                id,
                vendaRepository,
                movimentacaoService,
                caixaRepository,
                movimentacaoRepository
        );
        vendaCommandExecutor.executar(command);
    }

    private VendaResponse construirDto(Venda venda) {
        return VendaMapper.entidadeParaResponse(venda);
    }
}

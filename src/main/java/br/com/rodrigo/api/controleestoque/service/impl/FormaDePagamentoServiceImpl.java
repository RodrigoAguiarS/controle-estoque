package br.com.rodrigo.api.controleestoque.service.impl;

import br.com.rodrigo.api.controleestoque.conversor.FormaDePagamentoMapper;
import br.com.rodrigo.api.controleestoque.exception.MensagensError;
import br.com.rodrigo.api.controleestoque.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.controleestoque.model.FormaDePagamento;
import br.com.rodrigo.api.controleestoque.model.form.FormaDePagamentoForm;
import br.com.rodrigo.api.controleestoque.model.response.FormaDePagamentoResponse;
import br.com.rodrigo.api.controleestoque.repository.FormaDePagamentoRepository;
import br.com.rodrigo.api.controleestoque.service.IFormaDePagamento;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FormaDePagamentoServiceImpl implements IFormaDePagamento {

    private final FormaDePagamentoRepository formaDePagamentoRepository;
    private final FormaDePagamentoMapper formaDePagamentoMapper;

    @Override
    public FormaDePagamentoResponse criar(Long id, FormaDePagamentoForm formaDePagamentoForm) {
        FormaDePagamento formaDePagamento = criaFormaDePagamento(formaDePagamentoForm, id);
        formaDePagamento = formaDePagamentoRepository.save(formaDePagamento);
        return construirDto(formaDePagamento);
    }

    @Override
    public void deletar(Long id) {
        FormaDePagamento formaDePagamento = formaDePagamentoRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.FORMA_PAGAMENTO_NAO_ENCONTRADA.getMessage(id)));
        formaDePagamento.desativar();
        formaDePagamentoRepository.save(formaDePagamento);
    }

    @Override
    public Optional<FormaDePagamentoResponse> consultarPorId(Long id) {
        return formaDePagamentoRepository.findById(id).map(this::construirDto);
    }

    @Override
    public Page<FormaDePagamentoResponse> listarTodos(int page, int size, String sort, Long id, String nome,
                                                      String descricao, BigDecimal porcentagemAcrescimo) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort != null ? sort : "id"));
        Page<FormaDePagamento> formasDePagamento = formaDePagamentoRepository.findAll(id, nome, porcentagemAcrescimo,
                descricao, pageable);
        return formasDePagamento.map(this::construirDto);
    }

    private FormaDePagamento criaFormaDePagamento(FormaDePagamentoForm formaDePagamentoForm, Long id) {

        FormaDePagamento formaDePagamento = id == null ? new FormaDePagamento() : formaDePagamentoRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.TIPO_PRODUTO_NAO_ENCONTRADO.getMessage(id)));

        formaDePagamento.setNome(formaDePagamentoForm.getNome());
        formaDePagamento.setDescricao(formaDePagamentoForm.getDescricao());
        formaDePagamento.setPorcentagemAcrescimo(formaDePagamentoForm.getPorcentagemAcrescimo());
        return formaDePagamento;
    }

    private FormaDePagamentoResponse construirDto(FormaDePagamento formaDePagamento) {
        return formaDePagamentoMapper.entidadeParaResponse(formaDePagamento);
    }
}

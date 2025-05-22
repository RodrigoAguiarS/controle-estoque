package br.com.rodrigo.api.controleestoque.rest;

import br.com.rodrigo.api.controleestoque.model.form.FormaDePagamentoForm;
import br.com.rodrigo.api.controleestoque.model.response.FormaDePagamentoResponse;
import br.com.rodrigo.api.controleestoque.service.IFormaDePagamento;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/formaDePagamento")
@RequiredArgsConstructor
public class FormaDePagamentoController extends ControllerBase<FormaDePagamentoResponse> {

    private final IFormaDePagamento formaDePagamentoService;

    @PostMapping
    public ResponseEntity<FormaDePagamentoResponse> criar(@RequestBody @Valid FormaDePagamentoForm formaDePagamentoForm) {
        FormaDePagamentoResponse response = formaDePagamentoService.criar(null, formaDePagamentoForm);
        return responderItemCriadoComURI(response, ServletUriComponentsBuilder.fromCurrentRequest(), "/{id}", response.getId().toString());
    }

    @PutMapping("/{idFormaDePagamento}")
    public ResponseEntity<FormaDePagamentoResponse> atualizar(@PathVariable @Valid Long idFormaDePagamento, @RequestBody FormaDePagamentoForm formaDePagamentoForm) {
        FormaDePagamentoResponse response = formaDePagamentoService.criar(idFormaDePagamento, formaDePagamentoForm);
        return responderSucessoComItem(response);
    }

    @DeleteMapping("/{idFormaDePagamento}")
    public ResponseEntity<FormaDePagamentoResponse> deletar(@PathVariable Long idFormaDePagamento) {
        formaDePagamentoService.deletar(idFormaDePagamento);
        return responderSucesso();
    }

    @GetMapping("/{idFormaDePagamento}")
    public ResponseEntity<FormaDePagamentoResponse> consultarPorId(@PathVariable Long idFormaDePagamento) {
        Optional<FormaDePagamentoResponse> response = formaDePagamentoService.consultarPorId(idFormaDePagamento);
        return response.map(this::responderSucessoComItem)
                .orElseGet(this::responderItemNaoEncontrado);
    }

    @GetMapping()
    public ResponseEntity<Page<FormaDePagamentoResponse>> listarTodos(@RequestParam int page,
                                                            @RequestParam int size,
                                                            @RequestParam(required = false) String sort,
                                                            @RequestParam(required = false) Long id,
                                                            @RequestParam(required = false) String nome,
                                                            @RequestParam(required = false) BigDecimal porcentagemAcrescimo,
                                                            @RequestParam(required = false) String descricao) {
        Page<FormaDePagamentoResponse> formaDePagamentoResponsePage = formaDePagamentoService.listarTodos(page, size, sort, id, nome, descricao, porcentagemAcrescimo);
        return responderListaDeItensPaginada(formaDePagamentoResponsePage);
    }
}
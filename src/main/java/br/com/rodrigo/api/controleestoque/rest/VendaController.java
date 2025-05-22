package br.com.rodrigo.api.controleestoque.rest;

import br.com.rodrigo.api.controleestoque.model.form.VendaForm;
import br.com.rodrigo.api.controleestoque.model.response.VendaResponse;
import br.com.rodrigo.api.controleestoque.service.IVenda;
import br.com.rodrigo.api.controleestoque.util.DateConverterUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/vendas")
@RequiredArgsConstructor
public class VendaController extends ControllerBase<VendaResponse> {

    private final IVenda vendaService;

    @PostMapping
    public ResponseEntity<VendaResponse> criar(@RequestBody @Valid VendaForm vendaForm, UriComponentsBuilder uriBuilder) {
        VendaResponse response = vendaService.realizarVenda(vendaForm);
        return responderItemCriadoComURI(response, uriBuilder, "/tarefas/{id}", response.getId().toString());
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<VendaResponse>> listarTodos(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sort,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) BigDecimal valorMinimo,
            @RequestParam(required = false) BigDecimal valorMaximo,
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim,
            @RequestParam(required = false) Boolean ativo){

        LocalDateTime inicio = DateConverterUtil.parse(dataInicio, true);
        LocalDateTime fim = DateConverterUtil.parse(dataFim, false);

        return responderListaDeItensPaginada(
                vendaService.listarTodos(page, size, sort, id, valorMinimo, valorMaximo, inicio, fim, ativo));
    }

    @DeleteMapping("/cancelar/{idVenda}")
    public ResponseEntity<Void> cancelarVenda(@PathVariable Long idVenda) {
        vendaService.cancelarVenda(idVenda);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{idVenda}")
    public ResponseEntity<VendaResponse> consultarPorId(@PathVariable Long idVenda) {
        Optional<VendaResponse> response = vendaService.buscarPorId(idVenda);
        return response.map(this::responderSucessoComItem)
                .orElseGet(this::responderItemNaoEncontrado);
    }
}

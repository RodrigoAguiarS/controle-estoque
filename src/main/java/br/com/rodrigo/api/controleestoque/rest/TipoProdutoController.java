package br.com.rodrigo.api.controleestoque.rest;

import br.com.rodrigo.api.controleestoque.model.form.TipoProdutoForm;
import br.com.rodrigo.api.controleestoque.model.response.TipoProdutoResponse;
import br.com.rodrigo.api.controleestoque.service.ITipoProduto;
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
@RequestMapping("/tiposprodutos")
@RequiredArgsConstructor
public class TipoProdutoController extends ControllerBase<TipoProdutoResponse> {

    private final ITipoProduto tipoProdutoService;

    @PostMapping
    public ResponseEntity<TipoProdutoResponse> criar(@RequestBody @Valid TipoProdutoForm tipoProdutoForm) {
        TipoProdutoResponse response = tipoProdutoService.criar(tipoProdutoForm);
        return responderItemCriadoComURI(response, ServletUriComponentsBuilder.fromCurrentRequest(), "/{id}", response.getId().toString());
    }

    @PutMapping("/{idTipoProduto}")
    public ResponseEntity<TipoProdutoResponse> atualizar(@PathVariable @Valid Long idTipoProduto, @RequestBody TipoProdutoForm tipoProdutoForm) {
        TipoProdutoResponse response = tipoProdutoService.atualizar(idTipoProduto, tipoProdutoForm);
        return responderSucessoComItem(response);
    }

    @DeleteMapping("/{idTipoProduto}")
    public ResponseEntity<TipoProdutoResponse> deletar(@PathVariable Long idTipoProduto) {
        tipoProdutoService.deletar(idTipoProduto);
        return responderSucesso();
    }

    @GetMapping("/{idTipoProduto}")
    public ResponseEntity<TipoProdutoResponse> consultarPorId(@PathVariable Long idTipoProduto) {
        Optional<TipoProdutoResponse> response = tipoProdutoService.consultarPorId(idTipoProduto);
        return response.map(this::responderSucessoComItem)
                .orElseGet(this::responderItemNaoEncontrado);
    }

    @GetMapping()
    public ResponseEntity<Page<TipoProdutoResponse>> listarTodos(@RequestParam int page,
                                                            @RequestParam int size,
                                                            @RequestParam(required = false) String sort,
                                                            @RequestParam(required = false) Long id,
                                                            @RequestParam(required = false) String nome,
                                                            @RequestParam(required = false) BigDecimal margemLucro,
                                                            @RequestParam(required = false) String descricao) {
        Page<TipoProdutoResponse> tipoProdutoResponsePage = tipoProdutoService.listarTodos(page, size, sort, id, nome, margemLucro, descricao);
        return responderListaDeItensPaginada(tipoProdutoResponsePage);
    }
}
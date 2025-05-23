package br.com.rodrigo.api.controleestoque.rest;

import br.com.rodrigo.api.controleestoque.model.form.ProdutoForm;
import br.com.rodrigo.api.controleestoque.model.response.ProdutoResponse;
import br.com.rodrigo.api.controleestoque.service.IGraficos;
import br.com.rodrigo.api.controleestoque.service.IProduto;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController extends ControllerBase<ProdutoResponse> {

    private final IProduto produtoService;
    private final IGraficos graficosService;

    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(@RequestBody @Valid ProdutoForm produtoForm, UriComponentsBuilder uriBuilder) {
        ProdutoResponse response = produtoService.criar(null, produtoForm);
        return responderItemCriadoComURI(response, uriBuilder, "/tarefas/{idProduto}", response.getId().toString());
    }

    @PutMapping("/{idProduto}")
    public ResponseEntity<ProdutoResponse> atualizar(@PathVariable Long idProduto, @RequestBody @Valid ProdutoForm produtoForm) {
        ProdutoResponse response = produtoService.criar(idProduto, produtoForm);
        return responderSucessoComItem(response);
    }

    @DeleteMapping("/{idProduto}")
    public ResponseEntity<ProdutoResponse> deletar(@PathVariable Long idProduto) {
        produtoService.deletar(idProduto);
        return responderSucesso();
    }

    @GetMapping("/{idProduto}")
    public ResponseEntity<ProdutoResponse> consultarPorId(@PathVariable Long idProduto) {
        Optional<ProdutoResponse> response = produtoService.consultarPorId(idProduto);
        return response.map(this::responderSucessoComItem)
                .orElseGet(this::responderItemNaoEncontrado);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<ProdutoResponse>> listarTodos(@RequestParam int page,
                                                             @RequestParam int size,
                                                             @RequestParam(required = false) String sort,
                                                             @RequestParam(required = false) Long id,
                                                             @RequestParam(required = false) String descricao,
                                                             @RequestParam(required = false) BigDecimal valorFornecedor,
                                                             @RequestParam(required = false) Integer quantidadeEstoque,
                                                             @RequestParam(required = false) Long tipoProdutoId) {
        Page<ProdutoResponse> produtos = produtoService.listarTodos(page, size, sort, id, descricao, tipoProdutoId, valorFornecedor, quantidadeEstoque);
        return responderListaDeItensPaginada(produtos);
    }
}

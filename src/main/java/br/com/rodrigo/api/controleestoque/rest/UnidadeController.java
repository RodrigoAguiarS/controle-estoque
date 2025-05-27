package br.com.rodrigo.api.controleestoque.rest;

import br.com.rodrigo.api.controleestoque.model.form.UnidadeForm;
import br.com.rodrigo.api.controleestoque.model.response.UnidadeResponse;
import br.com.rodrigo.api.controleestoque.service.IUnidade;
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

import java.util.Optional;

@RestController
@RequestMapping("/unidades")
@RequiredArgsConstructor
public class UnidadeController extends ControllerBase<UnidadeResponse> {

    private final IUnidade unidadeService;

    @PostMapping
    public ResponseEntity<UnidadeResponse> criar(@RequestBody @Valid UnidadeForm unidadeForm) {
        UnidadeResponse response = unidadeService.criar( null, unidadeForm);
        return responderItemCriadoComURI(response, ServletUriComponentsBuilder.fromCurrentRequest(), "/{id}", response.getId().toString());
    }

    @PutMapping("/{idUnidade}")
    public ResponseEntity<UnidadeResponse> atualizar(@PathVariable @Valid Long idUnidade, @RequestBody UnidadeForm unidadeForm) {
        UnidadeResponse response = unidadeService.criar(idUnidade, unidadeForm);
        return responderSucessoComItem(response);
    }

    @DeleteMapping("/{idUnidade}")
    public ResponseEntity<UnidadeResponse> deletar(@PathVariable Long idUnidade) {
        unidadeService.deletar(idUnidade);
        return responderSucesso();
    }

    @GetMapping("/{idUnidade}")
    public ResponseEntity<UnidadeResponse> consultarPorId(@PathVariable Long idUnidade) {
        Optional<UnidadeResponse> response = unidadeService.buscarPorId(idUnidade);
        return response.map(this::responderSucessoComItem)
                .orElseGet(this::responderItemNaoEncontrado);
    }

    @GetMapping()
    public ResponseEntity<Page<UnidadeResponse>> listarTodos(@RequestParam int page,
                                                            @RequestParam int size,
                                                            @RequestParam(required = false) String sort,
                                                            @RequestParam(required = false) Long id,
                                                            @RequestParam(required = false) String nome,
                                                            @RequestParam(required = false) Long empresa) {
        Page<UnidadeResponse> unidadeResponsePage = unidadeService.listarTodos(page, size, sort, id, nome, empresa);
        return responderListaDeItensPaginada(unidadeResponsePage);
    }
}
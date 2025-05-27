package br.com.rodrigo.api.controleestoque.rest;

import br.com.rodrigo.api.controleestoque.model.form.PerfilForm;
import br.com.rodrigo.api.controleestoque.model.response.PerfilResponse;
import br.com.rodrigo.api.controleestoque.service.IPerfil;
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
@RequestMapping("/perfis")
@RequiredArgsConstructor
public class PerfilController extends ControllerBase<PerfilResponse> {

    private final IPerfil perfilService;

    @PostMapping
    public ResponseEntity<PerfilResponse> criar(@RequestBody @Valid PerfilForm perfilForm) {
        PerfilResponse response = perfilService.criar(null, perfilForm);
        return responderItemCriadoComURI(response, ServletUriComponentsBuilder.fromCurrentRequest(), "/{id}", response.getId().toString());
    }

    @PutMapping("/{idPerfil}")
    public ResponseEntity<PerfilResponse> atualizar(@PathVariable @Valid Long idPerfil, @RequestBody PerfilForm perfilForm) {
        PerfilResponse response = perfilService.criar(idPerfil, perfilForm);
        return responderSucessoComItem(response);
    }

    @DeleteMapping("/{idPerfil}")
    public ResponseEntity<PerfilResponse> deletar(@PathVariable Long idPerfil) {
        perfilService.deletar(idPerfil);
        return responderSucesso();
    }

    @GetMapping("/{idPerfil}")
    public ResponseEntity<PerfilResponse> consultarPorId(@PathVariable Long idPerfil) {
        Optional<PerfilResponse> response = perfilService.consultarPorId(idPerfil);
        return response.map(this::responderSucessoComItem)
                .orElseGet(this::responderItemNaoEncontrado);
    }

    @GetMapping()
    public ResponseEntity<Page<PerfilResponse>> listarTodos(@RequestParam int page,
                                                            @RequestParam int size,
                                                            @RequestParam(required = false) String sort,
                                                            @RequestParam(required = false) Long id,
                                                            @RequestParam(required = false) String nome,
                                                            @RequestParam(required = false) String descricao) {
        Page<PerfilResponse> perfilResponsePage = perfilService.consultarTodos(page, size, sort, id, nome, descricao);
        return responderListaDeItensPaginada(perfilResponsePage);
    }
}
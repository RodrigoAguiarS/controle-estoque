package br.com.rodrigo.api.controleestoque.rest;

import br.com.rodrigo.api.controleestoque.model.form.UsuarioForm;
import br.com.rodrigo.api.controleestoque.model.form.UsuarioResponse;
import br.com.rodrigo.api.controleestoque.service.IUsuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController extends ControllerBase<UsuarioResponse> {
    
    private final IUsuario usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(@RequestBody @Valid UsuarioForm usuarioForm) {
        UsuarioResponse response = usuarioService.criar(null, usuarioForm);
        return responderItemCriadoComURI(response, ServletUriComponentsBuilder.fromCurrentRequest(), "/{id}", response.getId().toString());
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable @Valid Long idUsuario, @RequestBody UsuarioForm usuarioForm) {
        UsuarioResponse response = usuarioService.criar(idUsuario, usuarioForm);
        return responderSucessoComItem(response);
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<UsuarioResponse> deletar(@PathVariable Long idUsuario) {
        usuarioService.deletar(idUsuario);
        return responderSucesso();
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioResponse> consultarPorId(@PathVariable Long idUsuario) {
        Optional<UsuarioResponse> response = usuarioService.buscarPorId(idUsuario);
        return response.map(this::responderSucessoComItem)
                .orElseGet(this::responderItemNaoEncontrado);
    }

    @GetMapping()
    public ResponseEntity<Page<UsuarioResponse>> listarTodos(@RequestParam int page,
                                                                 @RequestParam int size,
                                                                 @RequestParam(required = false) String sort,
                                                                 @RequestParam(required = false) String nome,
                                                                 @RequestParam(required = false) String email,
                                                                 @RequestParam(required = false) Long perfil,
                                                                 @RequestParam(required = false) Long unidade) {
        Page<UsuarioResponse> tipoProdutoResponsePage = usuarioService.listarTodos(page, size, sort, nome, email, perfil, unidade);
        return responderListaDeItensPaginada(tipoProdutoResponsePage);
    }

    @GetMapping("/papel")
    public ResponseEntity<List<String>> getRoles(Authentication authentication) {
        String email = authentication.getName();
        List<String> roles = usuarioService.obterPerfis(email);
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/logado")
    public ResponseEntity<UsuarioResponse> obterUsuarioLogado() {
        UsuarioResponse usuarioResponse = usuarioService.obterUsuarioLogado();
        return ResponseEntity.ok(usuarioResponse);
    }
}
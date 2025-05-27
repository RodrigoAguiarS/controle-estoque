package br.com.rodrigo.api.controleestoque.rest;

import br.com.rodrigo.api.controleestoque.model.form.EmpresaForm;
import br.com.rodrigo.api.controleestoque.model.response.EmpresaResponse;
import br.com.rodrigo.api.controleestoque.service.IEmpresa;
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
@RequestMapping("/empresas")
@RequiredArgsConstructor
public class EmpresaController extends ControllerBase<EmpresaResponse> {

    private final IEmpresa empresaService;

    @PostMapping
    public ResponseEntity<EmpresaResponse> criar(@RequestBody @Valid EmpresaForm empresaForm) {
        EmpresaResponse response = empresaService.criar( null, empresaForm);
        return responderItemCriadoComURI(response, ServletUriComponentsBuilder.fromCurrentRequest(), "/{id}", response.getId().toString());
    }

    @PutMapping("/{idEmpresa}")
    public ResponseEntity<EmpresaResponse> atualizar(@PathVariable @Valid Long idEmpresa, @RequestBody EmpresaForm empresaForm) {
        EmpresaResponse response = empresaService.criar(idEmpresa, empresaForm);
        return responderSucessoComItem(response);
    }

    @DeleteMapping("/{idEmpresa}")
    public ResponseEntity<EmpresaResponse> deletar(@PathVariable Long idEmpresa) {
        empresaService.deletar(idEmpresa);
        return responderSucesso();
    }

    @GetMapping("/{idEmpresa}")
    public ResponseEntity<EmpresaResponse> consultarPorId(@PathVariable Long idEmpresa) {
        Optional<EmpresaResponse> response = empresaService.buscarPorId(idEmpresa);
        return response.map(this::responderSucessoComItem)
                .orElseGet(this::responderItemNaoEncontrado);
    }

    @GetMapping()
    public ResponseEntity<Page<EmpresaResponse>> listarTodos(@RequestParam int page,
                                                            @RequestParam int size,
                                                            @RequestParam(required = false) String sort,
                                                            @RequestParam(required = false) Long id,
                                                            @RequestParam(required = false) String nome,
                                                            @RequestParam(required = false) String cnpj) {
        Page<EmpresaResponse> empresaResponsePage = empresaService.listarTodos(page, size, sort, id, nome, cnpj);
        return responderListaDeItensPaginada(empresaResponsePage);
    }
}
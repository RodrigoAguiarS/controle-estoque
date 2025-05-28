package br.com.rodrigo.api.controleestoque.rest;

import br.com.rodrigo.api.controleestoque.model.form.CaixaForm;
import br.com.rodrigo.api.controleestoque.repository.CaixaRepository;
import br.com.rodrigo.api.controleestoque.service.comand.caixa.AbrirCaixaCommand;
import br.com.rodrigo.api.controleestoque.service.comand.caixa.CaixaCommandExecutor;
import br.com.rodrigo.api.controleestoque.service.comand.caixa.FecharCaixaCommand;
import br.com.rodrigo.api.controleestoque.util.CaixaUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/caixas")
@RequiredArgsConstructor
public class CaixaController {

    private final CaixaCommandExecutor caixaCommandExecutor;
    private final CaixaRepository caixaRepository;
    private final CaixaUtil caixaUtil;

    @PostMapping("/abrir")
    public ResponseEntity<Void> abrirCaixa(@RequestBody CaixaForm caixaForm) {
        AbrirCaixaCommand abrirCaixaCommand = new AbrirCaixaCommand(caixaRepository, caixaForm.getValorInicial(), caixaUtil);
        caixaCommandExecutor.executar(abrirCaixaCommand);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{caixaId}/fechar")
    public ResponseEntity<Void> fecharCaixa(@PathVariable Long caixaId) {
        FecharCaixaCommand fecharCaixaCommand = new FecharCaixaCommand(caixaRepository, caixaId);
        caixaCommandExecutor.executar(fecharCaixaCommand);
        return ResponseEntity.ok().build();
    }
}

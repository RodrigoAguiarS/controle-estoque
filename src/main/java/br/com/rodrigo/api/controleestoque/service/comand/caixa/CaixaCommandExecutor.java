package br.com.rodrigo.api.controleestoque.service.comand.caixa;

import org.springframework.stereotype.Component;

@Component
public class CaixaCommandExecutor {

    public void executar(CaixaCommand command) {
        command.executar();
    }
}

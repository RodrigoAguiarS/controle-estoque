package br.com.rodrigo.api.controleestoque.service.comand.venda;

import org.springframework.stereotype.Component;

@Component
public class VendaCommandExecutor {

    public void executar(VendaCommand command) {
        command.executar();
    }
}

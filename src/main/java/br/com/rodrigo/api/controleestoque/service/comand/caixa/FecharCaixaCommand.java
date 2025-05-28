package br.com.rodrigo.api.controleestoque.service.comand.caixa;


import br.com.rodrigo.api.controleestoque.exception.MensagensError;
import br.com.rodrigo.api.controleestoque.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.controleestoque.model.Caixa;
import br.com.rodrigo.api.controleestoque.repository.CaixaRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FecharCaixaCommand implements CaixaCommand {

    private final CaixaRepository caixaRepository;
    private final Long caixaId;

    @Override
    public void executar() {
        Caixa caixa = caixaRepository.findById(caixaId)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.TIPO_PRODUTO_NAO_ENCONTRADO.getMessage()));
        caixa.desativar();
        caixaRepository.save(caixa);
    }
}

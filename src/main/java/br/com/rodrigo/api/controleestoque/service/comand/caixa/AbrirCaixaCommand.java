package br.com.rodrigo.api.controleestoque.service.comand.caixa;

import br.com.rodrigo.api.controleestoque.model.Caixa;
import br.com.rodrigo.api.controleestoque.model.Usuario;
import br.com.rodrigo.api.controleestoque.repository.CaixaRepository;
import br.com.rodrigo.api.controleestoque.util.CaixaUtil;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import static br.com.rodrigo.api.controleestoque.service.singleton.UsuarioContext.getUsuarioLogado;

@RequiredArgsConstructor
public class AbrirCaixaCommand implements CaixaCommand {

    private final CaixaRepository caixaRepository;
    private final BigDecimal valorInicial;
    private final CaixaUtil caixaUtil;

    @Override
    public void executar() {
        Usuario usuarioLogado = getUsuarioLogado();

        caixaUtil.validarValorInicial(valorInicial);
        caixaUtil.validarSeExisteCaixaAtivo(usuarioLogado.getId());

        Caixa caixa = Caixa.builder()
                .valorInicial(valorInicial)
                .valorAtual(valorInicial)
                .unidade(usuarioLogado.getUnidade())
                .usuario(usuarioLogado)
                .ativo(true)
                .build();
        caixaRepository.save(caixa);
    }
}
package br.com.rodrigo.api.controleestoque.util;

import br.com.rodrigo.api.controleestoque.exception.MensagensError;
import br.com.rodrigo.api.controleestoque.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.controleestoque.exception.ViolacaoIntegridadeDadosException;
import br.com.rodrigo.api.controleestoque.model.Caixa;
import br.com.rodrigo.api.controleestoque.model.Usuario;
import br.com.rodrigo.api.controleestoque.repository.CaixaRepository;
import br.com.rodrigo.api.controleestoque.service.singleton.UsuarioContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CaixaUtil {

    private final CaixaRepository caixaRepository;

    public Optional<Caixa> getCaixaDoUsuarioLogado() {
        Usuario usuarioLogado = UsuarioContext.getUsuarioLogado();
        if (usuarioLogado == null) {
            throw new ObjetoNaoEncontradoException(MensagensError.USUARIO_NAO_ENCONTRADO.getMessage());
        }
        return caixaRepository.findByUsuarioIdAndAtivoTrue(usuarioLogado.getId());
    }

    public void validarCaixaAtivo(Caixa caixa) {
        if (caixa == null || !caixa.getAtivo()) {
            throw new ObjetoNaoEncontradoException(MensagensError.CAIXA_NAO_ENCONTRADO.getMessage(
                    UsuarioContext.getUsuarioLogado().getId()));
        }
    }

    public void validarSeExisteCaixaAtivo(Long usuarioId) {
        boolean existeCaixaAtivo = caixaRepository.findByUsuarioIdAndAtivoTrue(usuarioId).isPresent();
        if (existeCaixaAtivo) {
            throw new ViolacaoIntegridadeDadosException(
                    MensagensError.CAIXA_JA_ABERTO.getMessage(usuarioId));
        }
    }

    public void validarValorInicial(BigDecimal valorInicial) {
        if (valorInicial == null || valorInicial.compareTo(BigDecimal.ZERO) < 0) {
            throw new ViolacaoIntegridadeDadosException(
                    MensagensError.CAIXA_SALDO_INVALIDO.getMessage(valorInicial));
        }
    }
}

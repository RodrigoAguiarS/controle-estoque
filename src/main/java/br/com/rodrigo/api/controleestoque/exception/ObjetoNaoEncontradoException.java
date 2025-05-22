package br.com.rodrigo.api.controleestoque.exception;

public class ObjetoNaoEncontradoException extends RuntimeException {
    public ObjetoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}

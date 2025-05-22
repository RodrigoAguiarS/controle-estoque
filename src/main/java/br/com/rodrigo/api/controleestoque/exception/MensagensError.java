package br.com.rodrigo.api.controleestoque.exception;

public enum MensagensError {


    TIPO_PRODUTO_NAO_ENCONTRADO("Tipo de produto não encontrado para o id %s"),
    VENDA_NAO_ENCONTRADA("Venda não encontrada para o id %s"),
    PRODUTO_NAO_ENCONTRADO("Produto não encontrado para o id %s");

    private final String message;

    MensagensError(String message) {
        this.message = message;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}

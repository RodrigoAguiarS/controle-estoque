package br.com.rodrigo.api.controleestoque.exception;

public enum MensagensError {


    TIPO_PRODUTO_NAO_ENCONTRADO("Tipo de produto não encontrado para o id %s"),
    TIPO_PRODUTO_NAO_PODE_SER_APAGADO("Tipo de produto não pode ser apagado, pois possui produtos associados");

    private final String message;

    MensagensError(String message) {
        this.message = message;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}

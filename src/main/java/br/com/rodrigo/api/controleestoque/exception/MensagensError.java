package br.com.rodrigo.api.controleestoque.exception;

public enum MensagensError {

    TIPO_PRODUTO_NAO_ENCONTRADO("Tipo de produto não encontrado para o id %s"),
    VENDA_NAO_ENCONTRADA("Venda não encontrada para o id %s"),
    MOVIMENTACAO_NAO_ENCONTRADA("Movimentação não encontrada para o id %s"),
    USUARIO_NAO_ENCONTRADO_POR_LOGIN("Usuário não encontrado para o login %s"),
    ESTOQUE_INSUFICIENTE("Estoque insuficiente para o produto %s"),
    PRODUTO_NAO_ENCONTRADO("Produto não encontrado para o id %s"),
    EMPRESA_NAO_ENCONTRADA("Empresa não encontrada para o id %s"),
    UNIDADE_NAO_ENCONTRADA("Unidade não encontrada para o id %s"),
    USUARIO_NAO_ENCONTRADO("Usuário não encontrado para o id %s"),
    PERFIL_NAO_ENCONTRADO("Perfil não encontrado para o id %s"),
    FORMA_PAGAMENTO_NAO_ENCONTRADA("Forma de pagamento não encontrada para o id %s"),
    PERFIL_EM_USO("Perfil não pode ser excluído, pois está em uso por um usuário com id %s"),
    USUARIO_ADMINISTRADOR_NAO_PODE_SER_DELETADO("Usuário administrador não pode ser deletado, id %s"),;

    private final String message;

    MensagensError(String message) {
        this.message = message;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}

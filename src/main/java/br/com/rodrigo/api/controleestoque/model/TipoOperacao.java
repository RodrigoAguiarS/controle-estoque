package br.com.rodrigo.api.controleestoque.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TipoOperacao {
    CADASTRO_PRODUTO("Cadastro de Produto"),
    VENDA("Venda"),
    CANCELAMENTO_VENDA("Cancelamento de Venda");

    private final String descricao;
}

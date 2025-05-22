package br.com.rodrigo.api.controleestoque.model;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TipoMovimentacao {
    ENTRADA("Entrada"),
    SAIDA("Saida");

    private final String descricao;
}

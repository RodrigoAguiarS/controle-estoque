package br.com.rodrigo.api.controleestoque.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "movimentacoes_estoque")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MovimentacaoEstoque extends EntidadeBase {

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipo;

    @Enumerated(EnumType.STRING)
    private TipoOperacao operacao;

    private BigDecimal valorMovimentacao;

    private int quantidade;
}

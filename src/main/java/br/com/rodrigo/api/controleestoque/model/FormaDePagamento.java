package br.com.rodrigo.api.controleestoque.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "forma-de-pagamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FormaDePagamento extends EntidadeBase {

    private String nome;
    private String descricao;

    private BigDecimal porcentagemAcrescimo;
}

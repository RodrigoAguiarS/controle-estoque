package br.com.rodrigo.api.controleestoque.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
public class Pessoa extends EntidadeBase {

    @Column(name = "nome", length = 50)
    private String nome;

    @Column(name = "telefone", length = 11)
    private String telefone;
}

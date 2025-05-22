package br.com.rodrigo.api.controleestoque.model.form;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VendaForm {
    @NotEmpty(message = "A lista de itens não pode estar vazia")
    @Valid
    private List<ItemVendaForm> itens;

    private Long formaDePagamentoId;

    private String observacao;
}

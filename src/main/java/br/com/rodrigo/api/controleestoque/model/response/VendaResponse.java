package br.com.rodrigo.api.controleestoque.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VendaResponse {
    private Long id;
    private List<ItemVendaResponse> itens;
    private BigDecimal valorTotal;
    private FormaDePagamentoResponse formaDePagamento;
    private String observacao;
    private LocalDateTime criadoEm;
    private Boolean ativo;
}

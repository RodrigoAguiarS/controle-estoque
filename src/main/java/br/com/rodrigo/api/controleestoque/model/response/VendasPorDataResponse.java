package br.com.rodrigo.api.controleestoque.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VendasPorDataResponse {
    private LocalDate data;
    private Long totalVendas;
    private BigDecimal valorTotal;
}

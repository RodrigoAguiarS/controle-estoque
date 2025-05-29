package br.com.rodrigo.api.controleestoque.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CaixaDetalhesResponse {
    private BigDecimal valorAtual;
    private BigDecimal valorEntrada;
    private BigDecimal valorSaida;
    private LocalDateTime dataAbertura;
    private boolean ativo;
}

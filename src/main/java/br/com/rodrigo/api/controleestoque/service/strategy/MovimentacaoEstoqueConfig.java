package br.com.rodrigo.api.controleestoque.service.strategy;

import br.com.rodrigo.api.controleestoque.model.TipoMovimentacao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;

@Configuration
public class MovimentacaoEstoqueConfig {

    @Bean
    public Map<TipoMovimentacao, MovimentacaoEstoqueStrategy> estrategiasMovimentacao(
            CadastroProdutoStrategy cadastroProdutoStrategy,
            SaidaVendaStrategy saidaStrategy) {

        Map<TipoMovimentacao, MovimentacaoEstoqueStrategy> estrategias = new EnumMap<>(TipoMovimentacao.class);
        estrategias.put(TipoMovimentacao.ENTRADA, cadastroProdutoStrategy);
        estrategias.put(TipoMovimentacao.SAIDA, saidaStrategy);
        return estrategias;
    }
}

package br.com.rodrigo.api.controleestoque.repository;

import br.com.rodrigo.api.controleestoque.model.Venda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public interface VendaRepository extends JpaRepository<Venda, Long> {

    @Query("""
    SELECT v
    FROM Venda v
    WHERE v.id = COALESCE(:id, v.id)
      AND v.valorTotal >= COALESCE(:valorMinimo, v.valorTotal)
      AND v.valorTotal <= COALESCE(:valorMaximo, v.valorTotal)
      AND v.criadoEm >= COALESCE(:dataInicio, v.criadoEm)
      AND v.criadoEm <= COALESCE(:dataFim, v.criadoEm)
      AND v.ativo = COALESCE(:ativo, v.ativo)
      AND v.formaDePagamento.id = COALESCE(:formaDePagamentoId, v.formaDePagamento.id)
    ORDER BY v.criadoEm DESC, v.id
""")
    Page<Venda> findAll(
            @Param("id") Long id,
            @Param("valorMinimo") BigDecimal valorMinimo,
            @Param("valorMaximo") BigDecimal valorMaximo,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            @Param("ativo") Boolean ativo,
            @Param("formaDePagamentoId") Long formaDePagamentoId,
            Pageable pageable
    );

}

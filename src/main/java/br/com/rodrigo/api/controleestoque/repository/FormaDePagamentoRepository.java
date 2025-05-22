package br.com.rodrigo.api.controleestoque.repository;

import br.com.rodrigo.api.controleestoque.model.FormaDePagamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface FormaDePagamentoRepository extends JpaRepository<FormaDePagamento, Long> {

    @Query("SELECT t FROM FormaDePagamento t " +
            "WHERE t.ativo = true " +
            "AND (:id IS NULL OR t.id = :id) " +
            "AND (:nome IS NULL OR LOWER(t.nome) LIKE %:nome%) " +
            "AND (:porcentagemAcrescimo IS NULL OR t.porcentagemAcrescimo = :porcentagemAcrescimo) " +
            "AND (:descricao IS NULL OR LOWER(t.descricao) LIKE %:descricao%) ")
    Page<FormaDePagamento> findAll(@Param("id") Long id,
                              @Param("nome") String nome,
                              @Param("porcentagemAcrescimo") BigDecimal porcentagemAcrescimo,
                              @Param("descricao") String descricao,
                              Pageable pageable);
}

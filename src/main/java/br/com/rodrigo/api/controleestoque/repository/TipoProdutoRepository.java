package br.com.rodrigo.api.controleestoque.repository;

import br.com.rodrigo.api.controleestoque.model.TipoProduto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface TipoProdutoRepository extends JpaRepository<TipoProduto, Long> {

    @Query("SELECT t FROM TipoProduto t " +
            "WHERE t.ativo = true " +
            "AND (:id IS NULL OR t.id = :id) " +
            "AND (:nome IS NULL OR LOWER(t.nome) LIKE %:nome%) " +
            "AND (:margemLucro IS NULL OR t.margemLucro = :margemLucro) " +
            "AND (:descricao IS NULL OR LOWER(t.descricao) LIKE %:descricao%) ")
    Page<TipoProduto> findAll(@Param("id") Long id,
                              @Param("nome") String nome,
                              @Param("margemLucro") BigDecimal margemLucro,
                              @Param("descricao") String descricao,
                              Pageable pageable);
}

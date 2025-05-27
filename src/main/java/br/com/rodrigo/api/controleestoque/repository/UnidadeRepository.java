package br.com.rodrigo.api.controleestoque.repository;

import br.com.rodrigo.api.controleestoque.model.Unidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface  UnidadeRepository extends JpaRepository<Unidade, Long> {

    @Query("SELECT u FROM Unidade u " +
            "JOIN u.empresa e " +
            "WHERE (:id IS NULL OR u.id = :id) " +
            "AND u.ativo = true " +
            "AND (:nome IS NULL OR LOWER(u.nome) LIKE %:nome%) " +
            "AND (:empresa IS NULL OR e.id = :empresa) " +
            "ORDER BY u.criadoEm DESC")
    Page<Unidade> findAll(@Param("id") Long id,
                          @Param("nome") String nome,
                          @Param("empresa") Long empresa,
                          Pageable pageable);

}

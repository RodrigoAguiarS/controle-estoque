package br.com.rodrigo.api.controleestoque.repository;

import br.com.rodrigo.api.controleestoque.model.Perfil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    @Query("SELECT p FROM Perfil p " +
            "WHERE p.ativo = true " +
            "AND (:id IS NULL OR p.id = :id) " +
            "AND (:nome IS NULL OR LOWER(p.nome) LIKE %:nome%) " +
            "AND (:descricao IS NULL OR LOWER(p.descricao) LIKE %:descricao%) ")
    Page<Perfil> findAll(@Param("id") Long id,
                              @Param("nome") String nome,
                              @Param("descricao") String descricao,
                              Pageable pageable);
}
package br.com.rodrigo.api.controleestoque.repository;

import br.com.rodrigo.api.controleestoque.model.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {


    @Query("SELECT e FROM Empresa e " +
            "WHERE e.ativo = true " +
            "AND (:id IS NULL OR e.id = :id) " +
            "AND (:nome IS NULL OR LOWER(e.nome) LIKE %:nome%) " +
            "AND (:cnpj IS NULL OR LOWER(e.cnpj) LIKE %:cnpj%) ")
    Page<Empresa> findAll(@Param("id") Long id,
                              @Param("nome") String nome,
                              @Param("cnpj") String cnpj,
                              Pageable pageable);
}

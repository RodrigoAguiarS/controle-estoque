package br.com.rodrigo.api.controleestoque.repository;
import br.com.rodrigo.api.controleestoque.model.Usuario;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailIgnoreCase(String email);

    Boolean existsByEmailIgnoreCase(String email);

    Boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    @Cacheable("usuarios")
    boolean existsByPerfisId(Long idPerfil);

    @Cacheable("usuarios")
    @Query("SELECT u FROM Usuario u " +
            "JOIN u.pessoa p " +
            "JOIN u.perfis pf " +
            "WHERE (:nome IS NULL OR LOWER(p.nome) LIKE %:nome%) " +
            "AND (:email IS NULL OR LOWER(u.email) LIKE %:email%) " +
            "AND (:unidade IS NULL OR u.unidade.id = :unidade) " +
            "AND (:perfil IS NULL OR pf.id = :perfil)")
    Page<Usuario> findAll(@Param("email") String email,
                          @Param("nome") String nome,
                          @Param("unidade") Long unidade,
                          @Param("perfil") Long perfil,
                          Pageable pageable);

    @Cacheable("usuarios")
    List<Usuario> findAllByPerfisId(long idPerfil);
}

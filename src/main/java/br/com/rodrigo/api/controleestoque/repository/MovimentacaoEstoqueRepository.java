package br.com.rodrigo.api.controleestoque.repository;

import br.com.rodrigo.api.controleestoque.model.MovimentacaoEstoque;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    @Query("SELECT me FROM MovimentacaoEstoque me " +
            "JOIN FETCH me.produto p " +
            "ORDER BY me.criadoEm DESC")
    List<MovimentacaoEstoque> buscarUltimasMovimentacoes(Pageable pageable);

}

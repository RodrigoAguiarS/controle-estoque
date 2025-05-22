package br.com.rodrigo.api.controleestoque.repository;

import br.com.rodrigo.api.controleestoque.model.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

}

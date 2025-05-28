package br.com.rodrigo.api.controleestoque.repository;

import br.com.rodrigo.api.controleestoque.model.MovimentacaoCaixa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentacaoCaixaRepository extends JpaRepository<MovimentacaoCaixa, Long> {
}
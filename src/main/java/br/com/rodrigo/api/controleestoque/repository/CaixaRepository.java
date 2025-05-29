package br.com.rodrigo.api.controleestoque.repository;

import br.com.rodrigo.api.controleestoque.model.Caixa;
import br.com.rodrigo.api.controleestoque.model.response.CaixaDetalhesResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CaixaRepository extends JpaRepository<Caixa, Long> {

    Optional<Caixa> findByUsuarioIdAndAtivoTrue(Long usuarioId);

    @Query("SELECT new br.com.rodrigo.api.controleestoque.model.response.CaixaDetalhesResponse(c.valorAtual, " +
            "(SELECT COALESCE(SUM(m.valor), 0) FROM MovimentacaoCaixa m WHERE m.caixa.id = c.id AND m.tipo = 'ENTRADA'), " +
            "(SELECT COALESCE(SUM(m.valor), 0) FROM MovimentacaoCaixa m WHERE m.caixa.id = c.id AND m.tipo = 'SAIDA'), " +
            "c.criadoEm, c.ativo) " +
            "FROM Caixa c WHERE c.usuario.id = :usuarioId AND c.ativo = true")
    Optional<CaixaDetalhesResponse> findCaixaInfoByUsuarioId(Long usuarioId);
}
package br.com.rodrigo.api.controleestoque.repository;

import br.com.rodrigo.api.controleestoque.model.Produto;
import br.com.rodrigo.api.controleestoque.model.response.ProdutoLucroResponse;
import br.com.rodrigo.api.controleestoque.model.response.TipoProdutoEstoqueResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT p FROM Produto p " +
            "JOIN p.tipoProduto tp " +
            "WHERE (:id IS NULL OR p.id = :id) " +
            "AND p.ativo = true " +
            "AND p.unidade.id = :unidadeId " +
            "AND (:descricao IS NULL OR LOWER(p.descricao) LIKE %:descricao%) " +
            "AND (:valorFornecedor IS NULL OR p.valorFornecedor = :valorFornecedor) " +
            "AND (:quantidadeEstoque IS NULL OR p.quantidadeEstoque = :quantidadeEstoque) " +
            "AND (:tipoProdutoId IS NULL OR tp.id = :tipoProdutoId) " +
            "ORDER BY p.criadoEm DESC")
    Page<Produto> findAll(@Param("id") Long id,
                          @Param("descricao") String descricao,
                          @Param("valorFornecedor") BigDecimal valorFornecedor,
                          @Param("quantidadeEstoque") Integer quantidadeEstoque,
                          @Param("tipoProdutoId") Long tipoProdutoId,
                          @Param("unidadeId") Long unidadeId,
                          Pageable pageable);

    @Query("SELECT new br.com.rodrigo.api.controleestoque.model.response.ProdutoLucroResponse(" +
            "   p.descricao, " +
            "   SUM(CASE WHEN me.tipo = 'SAIDA' THEN me.quantidade ELSE 0 END), " +
            "   SUM(CASE WHEN me.tipo = 'SAIDA' THEN (p.valorVenda - p.valorFornecedor) * me.quantidade ELSE 0 END)" +
            ") " +
            "FROM MovimentacaoEstoque me " +
            "LEFT JOIN me.produto p " +
            "WHERE me.unidade.id = :unidadeId " +
            "GROUP BY p.descricao")
    List<ProdutoLucroResponse> buscarLucroPorProduto(@Param("unidadeId") Long unidadeId);

    @Query("SELECT new br.com.rodrigo.api.controleestoque.model.response.TipoProdutoEstoqueResponse(" +
            "   tp.nome, " +
            "   SUM(CASE WHEN me.tipo = 'SAIDA' THEN me.quantidade ELSE 0 END), " +
            "   SUM(p.quantidadeEstoque)" +
            ") " +
            "FROM MovimentacaoEstoque me " +
            "JOIN me.produto.tipoProduto tp " +
            "LEFT JOIN me.produto p " +
            "WHERE me.unidade.id = :unidadeId " +
            "GROUP BY tp.nome")
    List<TipoProdutoEstoqueResponse> buscarEstoquePorTipoProduto(@Param("unidadeId") Long unidadeId);

}

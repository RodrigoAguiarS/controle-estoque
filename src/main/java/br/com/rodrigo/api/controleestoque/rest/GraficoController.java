package br.com.rodrigo.api.controleestoque.rest;
import br.com.rodrigo.api.controleestoque.model.MovimentacaoEstoque;
import br.com.rodrigo.api.controleestoque.model.response.ProdutoLucroResponse;
import br.com.rodrigo.api.controleestoque.model.response.TipoProdutoEstoqueResponse;
import br.com.rodrigo.api.controleestoque.service.IGraficos;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/graficos")
@RequiredArgsConstructor
public class GraficoController {

    private final IGraficos graficosService;

    @GetMapping("/lucro-por-produto")
    public ResponseEntity<List<ProdutoLucroResponse>> buscarLucroPorProduto() {
        List<ProdutoLucroResponse> resultado = graficosService.buscarLucroPorProduto();
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/estoque-por-tipo")
    public ResponseEntity<List<TipoProdutoEstoqueResponse>> buscarEstoquePorTipoProduto() {
        List<TipoProdutoEstoqueResponse> resultado = graficosService.buscarEstoquePorTipoProduto();
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/ultimas-movimentacoes")
    public ResponseEntity<List<MovimentacaoEstoque>> buscarUltimasMovimentacoes(@RequestParam(defaultValue = "3") int limite) {
        List<MovimentacaoEstoque> movimentacoes = graficosService.buscarUltimasMovimentacoes(limite);
        return ResponseEntity.ok(movimentacoes);
    }
}

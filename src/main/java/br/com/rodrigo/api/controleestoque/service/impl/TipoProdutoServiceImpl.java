package br.com.rodrigo.api.controleestoque.service.impl;

import br.com.rodrigo.api.controleestoque.exception.MensagensError;
import br.com.rodrigo.api.controleestoque.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.controleestoque.model.TipoProduto;
import br.com.rodrigo.api.controleestoque.model.form.TipoProdutoForm;
import br.com.rodrigo.api.controleestoque.model.response.TipoProdutoResponse;
import br.com.rodrigo.api.controleestoque.repository.TipoProdutoRepository;
import br.com.rodrigo.api.controleestoque.service.ITipoProduto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static br.com.rodrigo.api.controleestoque.conversor.TipoProdutoMapper.entidadeParaResponse;
import static br.com.rodrigo.api.controleestoque.service.singleton.UsuarioContext.getUsuarioLogado;

@Service
@RequiredArgsConstructor
public class TipoProdutoServiceImpl implements ITipoProduto {

    private final TipoProdutoRepository tipoProdutoRepository;

    @Override
    public TipoProdutoResponse criar(TipoProdutoForm tipoProdutoForm) {
        TipoProduto tipoProduto = criaTipoProduto(tipoProdutoForm, null);
        tipoProduto = tipoProdutoRepository.save(tipoProduto);
        return construirDto(tipoProduto);
    }

    @Override
    public TipoProdutoResponse atualizar(Long id, TipoProdutoForm tipoProdutoForm) {
        TipoProduto tipoProduto = criaTipoProduto(tipoProdutoForm, id);
        tipoProduto = tipoProdutoRepository.save(tipoProduto);
        return construirDto(tipoProduto);
    }

    private TipoProduto criaTipoProduto(TipoProdutoForm tipoProdutoForm, Long id) {
        TipoProduto tipoProduto = id == null ? new TipoProduto() : tipoProdutoRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.TIPO_PRODUTO_NAO_ENCONTRADO.getMessage(id)));
        tipoProduto.setNome(tipoProdutoForm.getNome());
        tipoProduto.setUnidade(getUsuarioLogado().getUnidade());
        tipoProduto.setDescricao(tipoProdutoForm.getDescricao());

        return tipoProduto;
    }

    private TipoProdutoResponse construirDto(TipoProduto tipoProduto) {
        return entidadeParaResponse(tipoProduto);
    }

    @Override
    public void deletar(Long id) {
        TipoProduto tipoProduto = tipoProdutoRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.TIPO_PRODUTO_NAO_ENCONTRADO.getMessage(id)));

        tipoProduto.desativar();
        tipoProdutoRepository.save(tipoProduto);
    }

    @Override
    public Optional<TipoProdutoResponse> consultarPorId(Long id) {
        return tipoProdutoRepository.findById(id).map(this::construirDto);
    }

    @Override
    public Page<TipoProdutoResponse> listarTodos(int page, int size, String sort, Long id, String nome, String descricao) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort != null ? sort : "id"));
        Page<TipoProduto> tipos = tipoProdutoRepository.findAllByUnidade(getUsuarioLogado().getUnidade().getId(), id, nome, descricao, pageable);
        return tipos.map(this::construirDto);
    }
}

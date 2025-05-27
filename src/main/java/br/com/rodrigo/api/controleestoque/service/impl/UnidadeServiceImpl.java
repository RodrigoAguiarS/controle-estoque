package br.com.rodrigo.api.controleestoque.service.impl;

import br.com.rodrigo.api.controleestoque.conversor.EmpresaMapper;
import br.com.rodrigo.api.controleestoque.conversor.UnidadeMapper;
import br.com.rodrigo.api.controleestoque.exception.MensagensError;
import br.com.rodrigo.api.controleestoque.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.controleestoque.model.Empresa;
import br.com.rodrigo.api.controleestoque.model.Unidade;
import br.com.rodrigo.api.controleestoque.model.form.UnidadeForm;
import br.com.rodrigo.api.controleestoque.model.response.EmpresaResponse;
import br.com.rodrigo.api.controleestoque.model.response.UnidadeResponse;
import br.com.rodrigo.api.controleestoque.repository.UnidadeRepository;
import br.com.rodrigo.api.controleestoque.service.IEmpresa;
import br.com.rodrigo.api.controleestoque.service.IUnidade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UnidadeServiceImpl implements IUnidade {

    private final UnidadeRepository unidadeRepository;
    private final IEmpresa empresaService;

    @Override
    public UnidadeResponse criar(Long id, UnidadeForm unidadeForm) {
        Unidade unidade = criaUnidade(unidadeForm, id);
        unidade = unidadeRepository.save(unidade);
        return construirDto(unidade);
    }

    @Override
    public Optional<UnidadeResponse> buscarPorId(Long id) {
        return unidadeRepository.findById(id)
                .map(this::construirDto);
    }

    @Override
    public Page<UnidadeResponse> listarTodos(int page, int size, String sort, Long id, String nome, Long empresa) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort != null ? sort : "nome"));
        Page<Unidade> unidades = unidadeRepository.findAll(id, nome, empresa, pageable);
        return unidades.map(this::construirDto);
    }

    @Override
    public void deletar(Long id) {
        Unidade unidade = unidadeRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.UNIDADE_NAO_ENCONTRADA.getMessage(id)));

        unidade.setAtivo(false);
        unidadeRepository.save(unidade);
    }

    private Unidade criaUnidade(UnidadeForm unidadeForm, Long idUnidade) {

        Unidade unidade = idUnidade == null ? new Unidade() : unidadeRepository.findById(idUnidade)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.UNIDADE_NAO_ENCONTRADA.getMessage(idUnidade)));

        EmpresaResponse empresaResponse = empresaService.buscarPorId(unidadeForm.getEmpresa())
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.EMPRESA_NAO_ENCONTRADA.getMessage(unidadeForm.getEmpresa())));

        Empresa empresa = EmpresaMapper.responseParaEntidade(empresaResponse);
        unidade.setNome(unidadeForm.getNome());
        unidade.setEmpresa(empresa);
        unidade.setTelefone(unidadeForm.getTelefone());

        return unidade;
    }

    private UnidadeResponse construirDto(Unidade unidade) {
        return UnidadeMapper.entidadeParaResponseComEmpresa(unidade);
    }
}

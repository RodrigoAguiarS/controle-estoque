package br.com.rodrigo.api.controleestoque.service.impl;

import br.com.rodrigo.api.controleestoque.conversor.EmpresaMapper;
import br.com.rodrigo.api.controleestoque.exception.MensagensError;
import br.com.rodrigo.api.controleestoque.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.controleestoque.model.Empresa;
import br.com.rodrigo.api.controleestoque.model.form.EmpresaForm;
import br.com.rodrigo.api.controleestoque.model.response.EmpresaResponse;
import br.com.rodrigo.api.controleestoque.repository.EmpresaRepository;
import br.com.rodrigo.api.controleestoque.service.IEmpresa;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmpresaServiceImpl implements IEmpresa {

    private final EmpresaRepository empresaRepository;

    @Override
    public EmpresaResponse criar(Long idEmpresa, EmpresaForm empresaForm) {
        Empresa empresa = criaEmpresa(empresaForm, idEmpresa);
        empresa = empresaRepository.save(empresa);
        return construirDto(empresa);
    }

    @Override
    public Optional<EmpresaResponse> buscarPorId(Long id) {
        return empresaRepository.findById(id)
                .map(this::construirDto);
    }

    @Override
    public Page<EmpresaResponse> listarTodos(int page, int size, String sort, Long id, String nome, String cnpj) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort != null ? sort : "nome"));
        Page<Empresa> empresas = empresaRepository.findAll(id, nome, cnpj, pageable);
        return empresas.map(this::construirDto);
    }

    @Override
    public void deletar(Long id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.EMPRESA_NAO_ENCONTRADA.getMessage(id)));

        empresa.setAtivo(false);
        empresaRepository.save(empresa);
    }

    private Empresa criaEmpresa(EmpresaForm empresaForm, Long idEmpresa) {

        Empresa empresa = idEmpresa == null ? new Empresa() : empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.EMPRESA_NAO_ENCONTRADA.getMessage(idEmpresa)));

        empresa.setNome(empresaForm.getNome());
        empresa.setCnpj(empresaForm.getCnpj());
        empresa.setAtivo(true);
        return empresa;
    }

    private EmpresaResponse construirDto(Empresa empresa) {
        return EmpresaMapper.entidadeParaResponse(empresa);
    }
}

package br.com.rodrigo.api.controleestoque.service.impl;

import br.com.rodrigo.api.controleestoque.conversor.PerfilMapper;
import br.com.rodrigo.api.controleestoque.exception.MensagensError;
import br.com.rodrigo.api.controleestoque.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.controleestoque.model.Perfil;
import br.com.rodrigo.api.controleestoque.model.form.PerfilForm;
import br.com.rodrigo.api.controleestoque.model.response.PerfilResponse;
import br.com.rodrigo.api.controleestoque.repository.PerfilRepository;
import br.com.rodrigo.api.controleestoque.service.IPerfil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PerfilServiceImpl implements IPerfil {

    private final PerfilRepository perfilRepository;

    @Override
    public PerfilResponse criar(Long id, PerfilForm perfilForm) {
        Perfil perfil = criarEntidade(perfilForm, null);
        perfil = perfilRepository.save(perfil);
        return construirDto(perfil);
    }

    @Override
    public void deletar(Long id) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.PERFIL_NAO_ENCONTRADO.getMessage(id)));
            perfil.setAtivo(false);
            perfilRepository.save(perfil);
    }

    @Override
    public Optional<PerfilResponse> consultarPorId(Long id) {
        return perfilRepository.findById(id).map(this::construirDto);
    }

    @Override
    public Page<PerfilResponse> consultarTodos(int page, int size, String sort, Long id, String nome, String descricao) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort != null ? sort : "nome"));
        Page<Perfil> perfis = perfilRepository.findAll(id, nome, descricao, pageable);
        return perfis.map(this::construirDto);
    }

    private Perfil criarEntidade(PerfilForm perfilForm, Long id) {
        Perfil perfil = id == null ? new Perfil() : perfilRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(MensagensError.PERFIL_NAO_ENCONTRADO.getMessage(id)));

        perfil.setNome(perfilForm.getNome());
        perfil.setDescricao(perfilForm.getDescricao());
        return perfil;
    }

    private PerfilResponse construirDto(Perfil perfil) {
        return PerfilMapper.entidadeParaResponse(perfil);
    }
}
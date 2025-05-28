package br.com.rodrigo.api.controleestoque.service.impl;

import br.com.rodrigo.api.controleestoque.conversor.PerfilMapper;
import br.com.rodrigo.api.controleestoque.conversor.UnidadeMapper;
import br.com.rodrigo.api.controleestoque.conversor.UsuarioMapper;
import br.com.rodrigo.api.controleestoque.exception.MensagensError;
import br.com.rodrigo.api.controleestoque.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.controleestoque.model.Perfil;
import br.com.rodrigo.api.controleestoque.model.Pessoa;
import br.com.rodrigo.api.controleestoque.model.Unidade;
import br.com.rodrigo.api.controleestoque.model.Usuario;
import br.com.rodrigo.api.controleestoque.model.form.UsuarioForm;
import br.com.rodrigo.api.controleestoque.model.form.UsuarioResponse;
import br.com.rodrigo.api.controleestoque.model.response.PerfilResponse;
import br.com.rodrigo.api.controleestoque.repository.PessoaRepository;
import br.com.rodrigo.api.controleestoque.repository.UsuarioRepository;
import br.com.rodrigo.api.controleestoque.service.IPerfil;
import br.com.rodrigo.api.controleestoque.service.IUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuario {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final PessoaRepository pessoaRepository;
    private final IPerfil perfilService;

    @Override
    public UsuarioResponse criar(Long id, UsuarioForm usuarioForm) {
        Usuario usuario = criarUsuario(id, usuarioForm);
        usuario = usuarioRepository.save(usuario);
        return construirDto(usuario);
    }

    @Override
    public Optional<UsuarioResponse> buscarPorId(Long id) {
        return usuarioRepository.findById(id).map(this::construirDto);
    }

    @Override
    public Page<UsuarioResponse> listarTodos(int page, int size, String sort, String nome, String email, Long perfil, Long unidade) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort != null ? sort : "p.nome"));
        Page<Usuario> usuarios = usuarioRepository.findAll(email, nome , unidade, perfil , pageable);
        return usuarios.map(this::construirDto);
    }

    @Override
    public void deletar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.USUARIO_NAO_ENCONTRADO.getMessage(id)));

        if (usuario.getPerfis().stream().anyMatch(perfil -> perfil.getId().equals(Perfil.ADMINSTRADOR))) {
            throw new ObjetoNaoEncontradoException(MensagensError.USUARIO_ADMINISTRADOR_NAO_PODE_SER_DELETADO.getMessage(id));
        }

        usuario.desativar();
        usuarioRepository.save(usuario);
    }

    @Override
    public UsuarioResponse obterUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return UsuarioMapper.entidadeParaResponse(obterUsuarioPorEmail(authentication.getName()));
    }

    @Override
    public List<String> obterPerfis(String email) {
        Usuario usuario = obterUsuarioPorEmail(email);
        return usuario.getPerfis().stream().map(Perfil::getNome).collect(Collectors.toList());
    }

    private Usuario obterUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.USUARIO_NAO_ENCONTRADO_POR_LOGIN.getMessage(email)));
    }

    private Usuario criarUsuario(Long idUsuario, UsuarioForm usuarioForm) {

        Usuario usuario = idUsuario == null ? new Usuario() : usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.USUARIO_NAO_ENCONTRADO.getMessage(idUsuario)));

        PerfilResponse perfilResponse = perfilService.consultarPorId(usuarioForm.getPerfil())
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.PERFIL_NAO_ENCONTRADO.getMessage(usuarioForm.getPerfil())));

        Unidade unidade = UnidadeMapper.responseParaEntidade(obterUsuarioLogado().getUnidade());

        Pessoa pessoa = usuario.getPessoa() == null ? new Pessoa() : usuario.getPessoa();
        pessoa.setNome(usuarioForm.getNome());
        pessoa.setTelefone(usuarioForm.getTelefone());
        pessoa.setUnidade(unidade);

        usuario.setPerfis(Set.of(PerfilMapper.responseParaEntidade(perfilResponse)));
        usuario.setPessoa(pessoaRepository.save(pessoa));
        usuario.setEmail(usuarioForm.getEmail());
        usuario.setUnidade(unidade);
        usuario.setSenha(passwordEncoder.encode(usuarioForm.getSenha()));

        return usuario;
    }

    private UsuarioResponse construirDto(Usuario usuario) {
        return UsuarioMapper.entidadeParaResponse(usuario);
    }
}

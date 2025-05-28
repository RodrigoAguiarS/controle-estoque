package br.com.rodrigo.api.controleestoque.conversor;

import br.com.rodrigo.api.controleestoque.model.Usuario;
import br.com.rodrigo.api.controleestoque.model.form.UsuarioResponse;

import java.util.stream.Collectors;

public class UsuarioMapper {

    public static UsuarioResponse entidadeParaResponse(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return UsuarioResponse.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .pessoa(PessoaMapper.entidadeParaResponse(usuario.getPessoa()))
                .unidade(UnidadeMapper.entidadeParaResponseComEmpresa(usuario.getUnidade()))
                .perfis(usuario.getPerfis().stream()
                        .map(PerfilMapper::entidadeParaResponse)
                        .collect(Collectors.toSet()))
                .ativo(usuario.getAtivo())
                .build();
    }

    public static Usuario responseParaEntidade(UsuarioResponse response) {
        if (response == null) {
            return null;
        }

        return Usuario.builder()
                .id(response.getId())
                .email(response.getEmail())
                .pessoa(PessoaMapper.responseParaEntidade(response.getPessoa()))
                .unidade(UnidadeMapper.responseParaEntidade(response.getUnidade()))
                .perfis(response.getPerfis().stream()
                        .map(PerfilMapper::responseParaEntidade)
                        .collect(Collectors.toSet()))
                .ativo(response.getAtivo())
                .build();
    }
}
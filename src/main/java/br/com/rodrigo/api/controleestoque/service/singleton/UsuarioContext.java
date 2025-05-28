package br.com.rodrigo.api.controleestoque.service.singleton;

import br.com.rodrigo.api.controleestoque.model.Usuario;

public class UsuarioContext {

    private static final ThreadLocal<Usuario> usuarioLogado = new ThreadLocal<>();

    private UsuarioContext() {

    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado.get();
    }

    public static void setUsuarioLogado(Usuario usuario) {
        usuarioLogado.set(usuario);
    }

    public static void limpar() {
        usuarioLogado.remove();
    }
}

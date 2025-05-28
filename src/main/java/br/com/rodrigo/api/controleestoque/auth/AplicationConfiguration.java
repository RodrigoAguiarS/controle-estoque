package br.com.rodrigo.api.controleestoque.auth;


import br.com.rodrigo.api.controleestoque.exception.MensagensError;
import br.com.rodrigo.api.controleestoque.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.controleestoque.model.Usuario;
import br.com.rodrigo.api.controleestoque.repository.UsuarioRepository;
import br.com.rodrigo.api.controleestoque.service.singleton.UsuarioContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AplicationConfiguration {

    private final UsuarioRepository userRepository;

    @Bean
    UserDetailsService userDetailsService() {
        return username -> {
            Usuario usuario = userRepository.findByEmailIgnoreCase(username)
                    .orElseThrow(() -> new ObjetoNaoEncontradoException(
                            MensagensError.USUARIO_NAO_ENCONTRADO_POR_LOGIN.getMessage(username)));

            UsuarioContext.setUsuarioLogado(usuario);

            return usuario;
        };
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

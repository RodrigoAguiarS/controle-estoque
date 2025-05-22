package br.com.rodrigo.api.controleestoque.conversor;

public interface IMapper<E, F, R> {
    R entidadeParaResponse(E entidade);
    E formParaEntidade(F form);
    E responseParaEntidade(R response);
}

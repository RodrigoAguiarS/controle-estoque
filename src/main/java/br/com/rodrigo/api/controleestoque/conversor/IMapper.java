package br.com.rodrigo.api.controleestoque.conversor;

public interface IMapper<E, R> {
    R entidadeParaResponse(E entidade);
    E responseParaEntidade(R response);
}

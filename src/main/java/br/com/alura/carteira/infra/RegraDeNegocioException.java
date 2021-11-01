package br.com.alura.carteira.infra;

public class RegraDeNegocioException extends RuntimeException {

    public RegraDeNegocioException(String message) {
        super(message);
    }
}

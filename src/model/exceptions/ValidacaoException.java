package model.exceptions;

import java.util.HashMap;

import java.util.Map;

public class ValidacaoException extends RuntimeException {

	/**
	 * Excessão para validar um formulário.
	 * Eu vou fazer essa excessão carregar as mensagens de erro do meu formulário
	 * Caso elas existam.
	 */
	
	private Map<String, String> erros = new HashMap<>();
	/*
	 * Estou fazendo isso pois vou ter que guarda os erros
	 * de cada campo do formulário.
	 * Para o campo nome o erro é esse.. 
	 * Então eu preciso de pares.
	 * Ai no primeiro <String, = Vai guardar o nome dos campos
	 * E no outro String> = Vai guardar as mensagens de erros.
	 */
	
	private static final long serialVersionUID = 1L;
	
	public ValidacaoException(String msg) {
		//passando para superclasse essa String
		super(msg);
	}
	
	//get dos erros
	public Map<String, String> getErros() {
		return erros;
	}
	
	//permitir adicionar o elemento na coleção.
	public void addError(String nomeCampo, String msgError) {
		erros.put(nomeCampo, msgError);
	}

}

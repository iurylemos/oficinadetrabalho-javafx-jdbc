package model.exceptions;

import java.util.HashMap;

import java.util.Map;

public class ValidacaoException extends RuntimeException {

	/**
	 * Excess�o para validar um formul�rio.
	 * Eu vou fazer essa excess�o carregar as mensagens de erro do meu formul�rio
	 * Caso elas existam.
	 */
	
	private Map<String, String> erros = new HashMap<>();
	/*
	 * Estou fazendo isso pois vou ter que guarda os erros
	 * de cada campo do formul�rio.
	 * Para o campo nome o erro � esse.. 
	 * Ent�o eu preciso de pares.
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
	
	//permitir adicionar o elemento na cole��o.
	public void addError(String nomeCampo, String msgError) {
		erros.put(nomeCampo, msgError);
	}

}

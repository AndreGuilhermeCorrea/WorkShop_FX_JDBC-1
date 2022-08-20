package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException  extends RuntimeException{

	/**
	 * exceção para validar um formulário responsável por carregar as mensagens de erro do formulário caso existam
	 */
	private static final long serialVersionUID = 1L;
	
	//atributo do tipo map para carregar os erros nessa exceção tipo map chave valor
	private Map<String, String> erros = new HashMap<>();

	//forçar instanciação da exceção com string
	public ValidationException(String msg) {
		super(msg);
	}
	
	public Map<String, String> getErros(){
		return erros;
	}
	
	//método para adicionar elemento na coleção
	public void addErros(String fieldNome, String erroMensagem) {
		erros.put(fieldNome, erroMensagem);
	}
}

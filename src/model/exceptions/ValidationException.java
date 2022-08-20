package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException  extends RuntimeException{

	/**
	 * exce��o para validar um formul�rio respons�vel por carregar as mensagens de erro do formul�rio caso existam
	 */
	private static final long serialVersionUID = 1L;
	
	//atributo do tipo map para carregar os erros nessa exce��o tipo map chave valor
	private Map<String, String> erros = new HashMap<>();

	//for�ar instancia��o da exce��o com string
	public ValidationException(String msg) {
		super(msg);
	}
	
	public Map<String, String> getErros(){
		return erros;
	}
	
	//m�todo para adicionar elemento na cole��o
	public void addErros(String fieldNome, String erroMensagem) {
		erros.put(fieldNome, erroMensagem);
	}
}

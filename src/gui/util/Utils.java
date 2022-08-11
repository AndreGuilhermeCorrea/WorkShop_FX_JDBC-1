package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	// classe responsável em retornar o palco Stage atual peritindo que se abra uma
	// janela apartir de um botão

	// função retorna stage o palco atual retornando o evento que o botao recebeu
	public static Stage currrentStage(ActionEvent event) {
		// implementação para acessar o stage apartir do objeto de evento com
		// downcasting NODE
		// acessando portanto o palco e a janela(superclasse do stage) também com
		// downcasting para poder ser convertida para stage
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}

	public static Integer tryParseToInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return null;
		}

	}
}

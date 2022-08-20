package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartamentoService;
import model.services.VendedoresService;

public class MainViewController implements Initializable {
	
	//atributos
	@FXML
	private MenuItem menuItemVendedores;
	@FXML
	private MenuItem menuItemDepartamento;
	@FXML
	private MenuItem menuItemAbout;
	
	//m�todos para tratar os eventos
	@FXML
	public void onMenuItemVendedoresAction() {
		loadView("/gui/VendedoresLista.fxml", (VendedoresListaControle controller) -> {
			controller.setVendedoresService(new VendedoresService());
			controller.updateTableView();
		});
	}

	@FXML
	//m�todos para tratar o evento com express�o lambda
	public void onMenuItemDepartamentoAction() {
		loadView("/gui/DepartamentoLista.fxml", (DepartamentoListaControle controller) -> {
			controller.setDepartamentoService(new DepartamentoService());
			controller.updateTableView();
		});
	}
	
	/*DepartamentoListaControle controller = loader.getController();
	controller.setDepartamentoService(new DepartamentoService());
	controller.updateTableView();*/
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {});
	}
	
	//m�todo da interface
	@Override
	public void initialize(URL uri, ResourceBundle rb) {	
	}
	
	//crud --- tela de cadastro CREATE/RETRIEVE/UPDATE/DELETE  CRIAR/LISTAR/ATUALIZAR/DELETAR
	
	
	//fun��o loadView gen�rica com a parametriza��o do consummer t para abrir outra tela e com synchronized para nao ser interrompida*
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
		//com exce��o tratada
		try {
		VBox newVbox = loader.load();
		
		//referencia da scene da classe main		
		Scene mainScene = Main.getMainScene();
		//associa��o do scene com o vbox da janela principal
		//m�todo getRoot acessa o primeiro elemento da view (scrollpane) antecedido de casting
		//ou seja referencia para o scrollpane antecedendo de outro casting para associar ao vbox
		VBox mainVBox = (VBox)((ScrollPane) mainScene.getRoot()).getContent();
		
		//referencia para o menu no primeiro filho da janela principal
		Node mainMenu = mainVBox.getChildren().get(0);
		//limpar todos os filhos do mainVBox
		mainVBox.getChildren().clear();
		//adicionar
		mainVBox.getChildren().add(mainMenu);
		mainVBox.getChildren().addAll(newVbox.getChildren());
		
		//comando para ativar initializingAction
		T controller = loader.getController();
		initializingAction.accept(controller);
		} 
		catch (IOException e) {
			Alerts.showAlert("IOException", "Erro ao carregar a p�gina", e.getMessage(), AlertType.ERROR);
		}
	}

	
	
}

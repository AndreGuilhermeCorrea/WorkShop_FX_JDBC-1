package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Departamento;
import model.services.DepartamentoService;

public class DepartamentoListaControle implements Initializable{
	
	//dependencia
	private DepartamentoService service;

	//referencia para o tableview
	@FXML
	private TableView<Departamento> tableViewdepartamento;
	
	@FXML
	private TableColumn<Departamento, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Departamento, String> tableColumnNome;
	
	@FXML
	private Button btNovo;
	
	//atributo da tabela departamento
	private ObservableList<Departamento> obsList;
	
	
	@FXML
	public void onBtNovoAction() {
		System.out.println("Bot�o a��o!");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		//m�todo auxiliar para iniciar componente
		initializeNodes();
		
	}
	
	//m�todo set para injetar a dependencia do Departamento Service(com invers�o de controle)
	public void setDepartamentoService(DepartamentoService service) {
		this.service = service;
	}

	//m�todo initializeNodes
	private void initializeNodes() {
		//padr�o para iniciar comportamento da coluna da tabela
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		
		//codigo para layout da tabela acompanhar janela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewdepartamento.prefHeightProperty().bind(stage.heightProperty());
	}

	//m�todo respons�vel por acessar servi�o carregar os departamentos e encaminha-los ao observalList
	public void updateTableView() {
		if (service ==null) {
			//caso o programador esque�a de lancar esse servi�o ser� lancada a exce��o
			throw new IllegalStateException("Erro!!! Servi�o Nulo!!!");
		}
		List<Departamento> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewdepartamento.setItems(obsList);
	}
}

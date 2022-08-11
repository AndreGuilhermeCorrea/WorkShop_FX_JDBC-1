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
		System.out.println("Botão ação!");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		//método auxiliar para iniciar componente
		initializeNodes();
		
	}
	
	//método set para injetar a dependencia do Departamento Service(com inversão de controle)
	public void setDepartamentoService(DepartamentoService service) {
		this.service = service;
	}

	//método initializeNodes
	private void initializeNodes() {
		//padrão para iniciar comportamento da coluna da tabela
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		
		//codigo para layout da tabela acompanhar janela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewdepartamento.prefHeightProperty().bind(stage.heightProperty());
	}

	//método responsável por acessar serviço carregar os departamentos e encaminha-los ao observalList
	public void updateTableView() {
		if (service ==null) {
			//caso o programador esqueça de lancar esse serviço será lancada a exceção
			throw new IllegalStateException("Erro!!! Serviço Nulo!!!");
		}
		List<Departamento> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewdepartamento.setItems(obsList);
	}
}

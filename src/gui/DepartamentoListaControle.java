package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
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
	
	//botao para cadastrar um novo departamento
	@FXML
	public void onBtNovoAction(ActionEvent event) {
		Stage parentStage = Utils.currrentStage(event);
		Departamento obj = new Departamento();
		createDialogForm(obj, "/gui/DepartamentoForm.fxml", parentStage);
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
	
	//método recebendo como parametro uma referencia para o stage da janela que criou a janela de dialogo
	private void createDialogForm(Departamento obj, String absoluteName, Stage parentStage) {
		//código para instanciar a janela de dialogo
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane painel = loader.load();
			
			
			//referencia para o controlador
			DepartamentoFormController controller = loader.getController();
			//injeção do controlador no departamento
			controller.setDepartamento(obj);
			controller.setDepartamentoService(new DepartamentoService());
			//carregar os dados do obj no formulário
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com os dados do departamento..");
			dialogStage.setScene(new Scene(painel));
			//codigo para janela poder ou nao ser redimencionada
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			//comportamento impedir que acessse outra janela sem antes fechar essa
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		catch (IOException e) {
			Alerts .showAlert("IOException", "ERRO ao carregar!", e.getMessage(), AlertType.ERROR);
			
		}
	}
}

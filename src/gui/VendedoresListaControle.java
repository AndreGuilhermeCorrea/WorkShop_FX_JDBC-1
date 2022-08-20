package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Vendedores;
import model.services.DepartamentoService;
import model.services.VendedoresService;

public class VendedoresListaControle implements Initializable, DataChangeListener {

	// dependencia
	private VendedoresService service;

	// atributo referencia para o tableview
	@FXML
	private TableView<Vendedores> tableViewdepartamento;

	// atributo
	@FXML
	private TableColumn<Vendedores, Integer> tableColumnId;

	// atributo
	@FXML
	private TableColumn<Vendedores, String> tableColumnNome;

	// atributo
	@FXML
	private TableColumn<Vendedores, String> tableColumnEmail;

	// atributo
	@FXML
	private TableColumn<Vendedores, Date> tableColumnDataNasc;

	// atributo
	@FXML
	private TableColumn<Vendedores, Double> tableColumnSalarioBase;

	// atributo
	@FXML
	private TableColumn<Vendedores, Vendedores> tableColumnEDIT;

	// atributo
	@FXML
	private TableColumn<Vendedores, Vendedores> tableColumnREMOVE;

	// atributo
	@FXML
	private Button btNovo;

	// atributo da tabela departamento
	private ObservableList<Vendedores> obsList;

	// botao para cadastrar um novo departamento
	@FXML
	public void onBtNovoAction(ActionEvent event) {
		Stage parentStage = Utils.currrentStage(event);
		Vendedores obj = new Vendedores();
		createDialogForm(obj, "/gui/VendedoresForm.fxml", parentStage);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// m�todo auxiliar para iniciar componente
		initializeNodes();

	}

	// m�todo set para injetar a dependencia do Vendedores Service(com invers�o de
	// controle)
	public void setVendedoresService(VendedoresService service) {
		this.service = service;
	}

	// m�todo initializeNodes
	private void initializeNodes() {
		// padr�o para iniciar comportamento da coluna da tabela
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnDataNasc.setCellValueFactory(new PropertyValueFactory<>("dataNasc")); 
			Utils.formatTableColumnDate(tableColumnDataNasc, "dd/MM/yyyy");
		tableColumnSalarioBase.setCellValueFactory(new PropertyValueFactory<>("salarioBase"));
			Utils.formatTableColumnDouble(tableColumnSalarioBase, 2);
		// codigo para layout da tabela acompanhar janela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewdepartamento.prefHeightProperty().bind(stage.heightProperty());
	}

	// m�todo respons�vel por acessar servi�o carregar os departamentos e
	// encaminha-los ao observalList
	public void updateTableView() {
		if (service == null) {
			// caso o programador esque�a de lancar esse servi�o ser� lancada a exce��o
			throw new IllegalStateException("Erro!!! Servi�o Nulo!!!");
		}
		List<Vendedores> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewdepartamento.setItems(obsList);
		// chamada do m�todo para inserir um bot�o editar em cada linha da tabela
		initEditButtons();
		// chamada do m�todo para inserir um bot�o excluir em cada linha da tabela
		initRemoveButtons();
	}

	// m�todo recebendo como parametro uma referencia para o stage da janela que
	// criou a janela de dialogo
	private void createDialogForm(Vendedores obj, String absoluteName, Stage parentStage) {
		// c�digo para instanciar a janela de dialogo
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane painel = loader.load();

			// referencia para o controlador
			VendedoresFormController controller = loader.getController();
			// inje��o do controlador no departamento
			controller.setVendedores(obj);
			// inje��o de dependencia do departamento service
			controller.setServices(new VendedoresService(), new DepartamentoService());
			//inje��o do m�todo para carregar a lista de departamentos
			controller.loadAssociatedObjects();
			// inje��o de dependencia do evento
			controller.subscribeDataChangeListener(this);
			// carregar os dados do obj no formul�rio
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com os dados do VENDEDOR.");
			dialogStage.setScene(new Scene(painel));
			// codigo para janela poder ou nao ser redimencionada
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			// comportamento impedir que acessse outra janela sem antes fechar essa
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", "ERRO ao carregar!", e.getMessage(), AlertType.ERROR);

		}
	}

	// m�todo padr�o
	@Override
	public void onDataChanged() {
		//
		updateTableView();

	}

	// m�todo para inserir um bot�o editar em cada linha da tabela
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Vendedores, Vendedores>() {

			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Vendedores obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				// ao pressionar o botao ser� aberto o formul�rio para edi��o
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/VendedoresForm.fxml", Utils.currrentStage(event)));
			}
		});
	}

	// m�todo para inserir bot�o remove em cada linha da tabela
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Vendedores, Vendedores>() {
			private final Button button = new Button("Excluir");

			@Override
			protected void updateItem(Vendedores obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				// ao pressionar o botao ser� aberto o formul�rio para edi��o
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Vendedores obj) {

		Optional<ButtonType> result = Alerts.showConfirmation("Confirma��o!!!", "Tem certeza que deseja excluir?");

		// teste se o resultado for confirmado
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Servi�o nulo!!!");
			}
			try {
				service.remove(obj);
				// atualizar os dados
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Erro!!!", null, e.getMessage(), AlertType.ERROR);
			}
		}

	}

}

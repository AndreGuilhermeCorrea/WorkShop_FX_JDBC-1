package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Departamento;
import model.entities.Vendedores;
import model.exceptions.ValidationException;
import model.services.DepartamentoService;
import model.services.VendedoresService;

public class VendedoresFormController implements Initializable {

	// dependencia para o vendedores
	private Vendedores entity;

	// dependencia para o vendedores service
	private VendedoresService service;

	// atributo dependecia para a lista de departamento
	private DepartamentoService departamentoService;

	// lista de objetos endereçados em receber os eventos da classe
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	// declaração dos componentes da tela
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtNome;
	@FXML
	private TextField txtEmail;
	@FXML
	private DatePicker dpDataNasc;
	@FXML
	private TextField txtSalarioBase;

	@FXML
	private ComboBox<Departamento> comboBoxDepartamento;

	@FXML
	private Label labelErroNome;
	@FXML
	private Label labelErroEmail;
	@FXML
	private Label labelErroDataNasc;
	@FXML
	private Label labelErroSalarioBase;

	@FXML
	private Button btSave;
	@FXML
	private Button btCancel;

	private ObservableList<Departamento> obsList;

	// método set da entity
	public void setVendedores(Vendedores entity) {
		this.entity = entity;
	}

	// método set da service
	public void setServices(VendedoresService service, DepartamentoService departamentoService) {
		this.service = service;
		this.departamentoService = departamentoService;
	}

	// método para inscrever (add) o listener na lista
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	// código para salvar departamento no banco
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		// programação defensiva
		if (entity == null) {
			throw new IllegalStateException("Valor nulo!");
		}
		if (service == null) {
			throw new IllegalStateException("Valor nulo!");
		}

		try {
			// chamada do método responsavel para acessar os dados do formulário e
			// instancialos no departamento
			entity = getFormData();
			// chamada do método para salvar ja injetado serviço no controller
			service.saveOrUpdate(entity);
			// chamada do método lista
			notifyDataChangeListeners();
			// função para fechar a janela após salvamento
			Utils.currrentStage(event).close();
		} catch (ValidationException e) {
			setErroMensagens(e.getErros());
		} catch (DbException e) {
			Alerts.showAlert("Erro ao salvar!", null, e.getMessage(), AlertType.ERROR);
		}
	}

	// método para chamar a lista
	public void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}

	}

	// método acessa os dados do formulário e carregará um objeto a esses dados
	private Vendedores getFormData() {
		Vendedores obj = new Vendedores();
		// instanciação da exceção
		ValidationException excecao = new ValidationException("Erro de validação!");

		// função que acessa o id na caixa convertido
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		// campo do nome nao pode ser vazio portanto exceção com teste
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			excecao.addErros("nome", "O campo não pode estar vazio!!!");
		}
		// campo do nome nao pode ser vazio portanto exceção
		obj.setNome(txtNome.getText());
		
		

		// campo do Email nao pode ser vazio portanto exceção com teste
		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			excecao.addErros("email", "O campo não pode estar vazio!!!");
		}
		// campo do Email nao pode ser vazio portanto exceção
		obj.setEmail(txtEmail.getText());
		
		if (dpDataNasc.getValue() == null) {
			excecao.addErros("dataNasc", "O campo não pode estar vazio!!!");
		}
		else {
			Instant instante = Instant.from(dpDataNasc.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataNasc(Date.from(instante));
		}
		
		// campo do Salário nao pode ser vazio portanto exceção com teste
		if (txtSalarioBase.getText() == null || txtSalarioBase.getText().trim().equals("")) {
			excecao.addErros("salarioBase", "O campo não pode estar vazio!!!");
		}
		obj.setSalarioBase(Utils.tryParseToDouble(txtSalarioBase.getText()));
		
		obj.setDepartamento(comboBoxDepartamento.getValue());
		
		// campo do nome nao pode ser vazio portanto exceção com teste
		if (excecao.getErros().size() > 0) {
			throw excecao;
		}
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		// função para fechar a janela ao pressionar o botao cancelar
		Utils.currrentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	// métodos para restriçoes
	public void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 70);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpDataNasc, "dd/MM/yyyy");
		Constraints.setTextFieldDouble(txtSalarioBase);

		initializeComboBoxDepartamento();

	}

	// método responsavel por acessar os dados do departamento e popular as caixas
	// de texto do formulário
	public void updateFormData() {
		// programação defensiva com teste
		if (entity == null) {
			throw new IllegalStateException("Valor Nulo!!!");
		}
		// inserindo nas caixas de texto os dados que estao instanciados na entity e ja
		// com a conversão do id para string
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtSalarioBase.setText(String.format("%.2f", entity.getSalarioBase()));
		if (entity.getDataNasc() != null) {
			dpDataNasc.setValue(LocalDate.ofInstant(entity.getDataNasc().toInstant(), ZoneId.systemDefault()));
		}
		// teste para saber se o vendedor é novo
		if (entity.getDepartamento() == null) {
			comboBoxDepartamento.getSelectionModel().selectFirst();
		} else {
			// prencher o combobox com o departamento
			comboBoxDepartamento.setValue(entity.getDepartamento());
		}
	}

	// método responsavel por carregar lista de departamento no combobox com
	// programação defensiva
	public void loadAssociatedObjects() {
		if (departamentoService == null) {
			throw new IllegalStateException("Departamento esta nulo!");
		}
		// carrega os departamentos
		List<Departamento> list = departamentoService.findAll();
		// insere a lista de departamentos carregados no combo
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartamento.setItems(obsList);
	}

	// metodo para prencher a mensagem de erro no label criado
	private void setErroMensagens(Map<String, String> erros) {
		// conjunto set
		Set<String> fields = erros.keySet();
			
		labelErroNome.setText((fields.contains("nome") ? erros.get("nome") : ""));
		
		
		labelErroEmail.setText((fields.contains("email") ? erros.get("email") : ""));
		
		labelErroDataNasc.setText((fields.contains("dataNasc") ? erros.get("dataNasc") : ""));
		
		labelErroSalarioBase.setText((fields.contains("salarioBase") ? erros.get("salarioBase") : ""));
		
	}

	private void initializeComboBoxDepartamento() {
		Callback<ListView<Departamento>, ListCell<Departamento>> factory = lv -> new ListCell<Departamento>() {
			@Override
			protected void updateItem(Departamento item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		comboBoxDepartamento.setCellFactory(factory);
		comboBoxDepartamento.setButtonCell(factory.call(null));
	}

}

package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Departamento;
import model.exceptions.ValidationException;
import model.services.DepartamentoService;

public class DepartamentoFormController implements Initializable{

	//dependencia para o departamento
	private Departamento entity;
	
	//dependencia para o departamento service
	private DepartamentoService service;
	
	//lista de objetos endereçados em receber os eventos da classe
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	//declaração dos componentes da tela
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtNome;
	@FXML
	private Label labelErroNome;
	@FXML
	private Button btSave;
	@FXML
	private Button btCancel;
	
	//método set da entity
	public void setDepartamento(Departamento entity) {
		this.entity = entity;
	}
	
	//método set da service
	public void setDepartamentoService(DepartamentoService service) {
		this.service = service;
	}
	//método para inscrever (add) o listener na lista
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	//código para salvar departamento no banco
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		//programação defensiva
		if (entity == null) {
			throw new IllegalStateException("Valor nulo!");
		}
		if (service == null) {
			throw new IllegalStateException("Valor nulo!");
		}
		
		try {
			//chamada do método responsavel para acessar os dados do formulário e instancialos no departamento
			entity = getFormData();
			//chamada do método para salvar ja injetado serviço no controller
			service.saveOrUpdate(entity);
			//chamada do método lista
			notifyDataChangeListeners();
			//função para fechar a janela após salvamento
			Utils.currrentStage(event).close();
		}
		catch (ValidationException e) {
			setErroMensagens(e.getErros());
		}
		catch (DbException e) {
			Alerts.showAlert("Erro ao salvar!", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	//método para chamar a lista
	public void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
		
	}
	
	//método acessa os dados do formulário re retornará um novo objeto
	private Departamento getFormData() {
		Departamento obj = new Departamento();
		//instanciação da exceção
		ValidationException excecao = new ValidationException("Erro de validação!");
		
		//função que acessa o id na caixa convertido 
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		//campo do nome nao pode ser vazio portanto exceção com teste
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			excecao.addErros("nome", "O campo não pode estar vazio!!!");
		}
		//campo do nome nao pode ser vazio portanto exceção
		obj.setNome(txtNome.getText());
		//campo do nome nao pode ser vazio portanto exceção com teste
		if (excecao.getErros().size()>0) {
			throw excecao;
		}
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		//função para fechar a janela ao pressionar o botao cancelar
		Utils.currrentStage(event).close();
	}

	
	@Override
	public void initialize(URL url, ResourceBundle rb) {	
		initializeNodes();
	}

	
	//métodos para restriçoes
	public void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 30);
	}
	
	//método responsavel por acessar os dados do departamento e popular as caixas de texto do formulário
	public void updateFormData() {
		//programação defensiva com teste
		if (entity == null) {
			throw new IllegalStateException("Valor Nulo!!!");
		}
		//inserindo nas caixas de texto os dados que estao instanciados na entity e ja com a conversão do id para string
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
	}
	
	//metodo para prencher a mensagem de erro no label criado
	private void setErroMensagens(Map<String, String> erros) {
		//conjunto set
		Set<String> fields = erros.keySet();
		if (fields.contains("nome")) {
			labelErroNome.setText(erros.get("nome"));
		}
	}
	
	
}

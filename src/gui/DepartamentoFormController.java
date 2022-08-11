package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
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
import model.services.DepartamentoService;

public class DepartamentoFormController implements Initializable{

	//dependencia para o departamento
	private Departamento entity;
	
	//dependencia para o departamento service
	private DepartamentoService service;
	
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
	
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Valor nulo!");
		}
		if (service == null) {
			throw new IllegalStateException("Valor nulo!");
		}
		
		try {
			//método responsavel para acessar os dados do formulário e instancialos no departamento
			entity = getFormData();
			service.saveOrUpdate(entity);
			//função para fechar a janela após salvamento
			Utils.currrentStage(event).close();
		}
		catch (DbException e) {
			Alerts.showAlert("Erro ao salvar!", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private Departamento getFormData() {
		Departamento obj = new Departamento();
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setNome(txtNome.getText());
		
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		//função para fechar a janela após salvamento
		Utils.currrentStage(event).close();
	}

	
	@Override
	public void initialize(URL url, ResourceBundle rb) {		
	}

	
	//métodos para restriçoes
	public void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 40);
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
	
	
}

package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entidades.Departamento;

public class DepartamentoFormController implements Initializable {
	
	//Deped�ncia para o Departamento.
	//Entidade relacionada a essa formul�rio.
	private Departamento entidadeDepartamento;
	
	
	
	
	//Declara��o dos componentes na tela
	//O que vou controlar.
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private Label labelErrorNome;
	
	@FXML
	private Button btSalvar;
	
	@FXML
	private Button btCancelar;
	
	//Implementar o set do Departamento.
	public void setDepartamento(Departamento entidadeDepartamento) {
		this.entidadeDepartamento = entidadeDepartamento;
	}
	
	
	
	//Metodos para as a��es
	@FXML
	public void onBtSalvarAcao() {
		System.out.println("onBtSalvarAcao");
	}
	
	@FXML
	public void onBtCancelarAcao() {
		System.out.println("onBtCancelarAcao");
	}
	
	
	

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializarNodes();
		
	}

	//Fun��o auxiliar
	private void inicializarNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 30);
	}
	
	
	public void atualizarDadosFormulario() {
		//Programa��o defensiva
		//Vai que o programador esqueceu de injetar o departamento..
		if(entidadeDepartamento == null) {
			throw new IllegalStateException("EntidadeDepartamento est� nula.");
		}
		
		
		
		//Jogar nas caixinha de texto, os dados que est�o no tipo DepartamentoEntidade
		//Por que valueOf? por que a caixinha de texto trabalha com String
		//Ou seja vou ter que converter o entidadeDepartamento.getID que � inteiro
		//Para STRING
		txtId.setText(String.valueOf(entidadeDepartamento.getId()));
		//
		txtNome.setText(entidadeDepartamento.getNome());
	}
}

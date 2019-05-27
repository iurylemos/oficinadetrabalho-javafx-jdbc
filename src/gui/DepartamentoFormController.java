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
import model.entidades.Departamento;
import model.servicos.DepartamentoServico;

public class DepartamentoFormController implements Initializable {
	
	//Depedência para o Departamento.
	//Entidade relacionada a essa formulário.
	private Departamento entidadeDepartamento;
	
	//Depedencia com Departamento Servico
	private DepartamentoServico servico;
	
	
	
	
	//Declaração dos componentes na tela
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
	
	public void setDepartamentoServico(DepartamentoServico servico) {
		this.servico = servico;
	}
	
	
	
	//Metodos para as ações
	@FXML
	public void onBtSalvarAcao(ActionEvent evento) {
		//Programação defensiva.
		if(entidadeDepartamento == null) {
			//Ou seja vai que o programador esqueceu de enjetar..
			throw new IllegalStateException("Entidade está nulo");
		}
		if(servico == null) {
			throw new IllegalStateException("Servico está nulo");
		}
		//Vou colocar tudo isso aqui abaixo dentro do try
		try {
			//Como se trata de um BD, pode gerar uma excessão.
			//Salva o departamento no BD
			//o meu departamento vai receber o getFormData
			//Responsável por pegar os dados que estão na caixinha do formulário
			//E instanciar um departamento para mim.
			entidadeDepartamento = getFormData();
			//Salvei no banco de dados ou atualizei
			servico.salvarOuAtualizar(entidadeDepartamento);
			//Fechando a janela.
			Utils.palcoAtual(evento).close();
		}catch (DbException e) {
			Alerts.showAlert("Error salvando objeto", null, e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	private Departamento getFormData() {
		//pegar os dados do formulário.
		/*
		 * Como no ID que o usuário vai escrever na telinha
		 * está dentro da variavel TxtId
		 * peguei o texto que está lá para pegar os dados do formulário
		 * e utilizei o getText
		 * E coloquei o metodo tryParseToInt
		 * para converter uma string para inteiro.
		 * Se retornar nulo, vai  ser um valor pra inserção.
		 */
		Departamento obj = new Departamento();
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		//Como o Nome já é String vou pegar direto
		obj.setNome(txtNome.getText());
		
		return obj;
	}
	

	@FXML
	public void onBtCancelarAcao(ActionEvent evento) {
		Utils.palcoAtual(evento).close();
	}
	
	
	

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializarNodes();
		
	}

	//Função auxiliar
	private void inicializarNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 30);
	}
	
	
	public void atualizarDadosFormulario() {
		//Programação defensiva
		//Vai que o programador esqueceu de injetar o departamento..
		if(entidadeDepartamento == null) {
			throw new IllegalStateException("EntidadeDepartamento está nula.");
		}
		
		
		
		//Jogar nas caixinha de texto, os dados que estão no tipo DepartamentoEntidade
		//Por que valueOf? por que a caixinha de texto trabalha com String
		//Ou seja vou ter que converter o entidadeDepartamento.getID que é inteiro
		//Para STRING
		txtId.setText(String.valueOf(entidadeDepartamento.getId()));
		//
		txtNome.setText(entidadeDepartamento.getNome());
	}
}

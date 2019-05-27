package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listener.DataChangeListener;
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
	
	//Deped�ncia para o Departamento.
	//Entidade relacionada a essa formul�rio.
	private Departamento entidadeDepartamento;
	
	//Depedencia com Departamento Servico
	private DepartamentoServico servico;
	
	//Lista sobre modifica��es de dados.
	//O meu controlador agora tem uma lista de mofica��es de dados.
	
	//Ele vai permitir outros objetos se increverem nessa lista
	//E receberem o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	
	
	
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
	
	public void setDepartamentoServico(DepartamentoServico servico) {
		this.servico = servico;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		/*
		 * Agora outros objetos, desde que eles implementem essa interface
		 * que � DatachangeListener eles podem se inscrever para receber
		 * o evento da minha classe.
		 */
		//Esse metodo vai ter que inscrever esse listener na minha lista.
		dataChangeListeners.add(listener);
	}
	
	
	
	//Metodos para as a��es
	@FXML
	public void onBtSalvarAcao(ActionEvent evento) {
		//Programa��o defensiva.
		if(entidadeDepartamento == null) {
			//Ou seja vai que o programador esqueceu de enjetar..
			throw new IllegalStateException("Entidade est� nulo");
		}
		if(servico == null) {
			throw new IllegalStateException("Servico est� nulo");
		}
		//Vou colocar tudo isso aqui abaixo dentro do try
		try {
			//Como se trata de um BD, pode gerar uma excess�o.
			//Salva o departamento no BD
			//o meu departamento vai receber o getFormData
			//Respons�vel por pegar os dados que est�o na caixinha do formul�rio
			//E instanciar um departamento para mim.
			entidadeDepartamento = getFormData();
			//Salvei no banco de dados ou atualizei
			servico.salvarOuAtualizar(entidadeDepartamento);
			//criei esse metodo abaixo, que emite um evento na lista.
			notificacaoDataChangeListeners();
			//Fechando a janela.
			Utils.palcoAtual(evento).close();
		}catch (DbException e) {
			Alerts.showAlert("Error salvando objeto", null, e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	private void notificacaoDataChangeListeners() {
		//Executando o onDataChanged(); em cada um dos listerners
		//Em outras palavras vou emitir esse evento que � o onDataChanged() aqui para os meus listeners
		/*
		 * Para cada listeners pertencente ao meu dataChangeListeners
		 * vai pegar o listener e botar onDataChanged()
		 * 
		 */
		for(DataChangeListener listener: dataChangeListeners) {
			listener.onDataChanged();
		}
		
		
	}

	private Departamento getFormData() {
		//pegar os dados do formul�rio.
		/*
		 * Como no ID que o usu�rio vai escrever na telinha
		 * est� dentro da variavel TxtId
		 * peguei o texto que est� l� para pegar os dados do formul�rio
		 * e utilizei o getText
		 * E coloquei o metodo tryParseToInt
		 * para converter uma string para inteiro.
		 * Se retornar nulo, vai  ser um valor pra inser��o.
		 */
		Departamento obj = new Departamento();
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		//Como o Nome j� � String vou pegar direto
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

package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

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
import model.exceptions.ValidacaoException;
import model.servicos.DepartamentoServico;

public class DepartamentoFormController implements Initializable {
	
	//Depedência para o Departamento.
	//Entidade relacionada a essa formulário.
	private Departamento entidadeDepartamento;
	
	//Depedencia com Departamento Servico
	private DepartamentoServico servico;
	
	//Lista sobre modificações de dados.
	//O meu controlador agora tem uma lista de moficações de dados.
	
	//Ele vai permitir outros objetos se increverem nessa lista
	//E receberem o evento
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	
	
	
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
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		/*
		 * Agora outros objetos, desde que eles implementem essa interface
		 * que é DatachangeListener eles podem se inscrever para receber
		 * o evento da minha classe.
		 */
		//Esse metodo vai ter que inscrever esse listener na minha lista.
		dataChangeListeners.add(listener);
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
			//criei esse metodo abaixo, que emite um evento na lista.
			notificacaoDataChangeListeners();
			//Fechando a janela.
			Utils.palcoAtual(evento).close();
		}catch(ValidacaoException e) {
			setMensagemErros(e.getErros());
				
		}catch (DbException e) {
			Alerts.showAlert("Error salvando objeto", null, e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	private void notificacaoDataChangeListeners() {
		//Executando o onDataChanged(); em cada um dos listerners
		//Em outras palavras vou emitir esse evento que é o onDataChanged() aqui para os meus listeners
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
		//Utilizar o objeto para um erro de excessao.
		ValidacaoException excessao = new ValidacaoException("Erro na Validação");
		
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		//Como o Nome já é String vou pegar direto
		//Se o texto estiver nulo, ou se o texto eliminando os espaços em branco
		//Seja no inicio ou no final com o TRIM se isso aqui for igual
		//ao String vázio. Significa que a minha caixinha está vazia.
			if(txtNome.getText() == null || txtNome.getText().trim().equals("")) {
				excessao.addError("name", "O campo não pode ser vázio");
			}
			//Mesmo vázio eu vou setar.
			obj.setNome(txtNome.getText());
		
			/*
			 * Agora vou testar se na minha coleção de erros
			 * Tem pelo menos um erro lá
			 * se isso aqui for verdade vou lançar a minha excessao.
			 */
			if(excessao.getErros().size() > 0) {
				throw excessao;
			}
		
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
	
	/*
	 * Vou passar como argumento o map
	 * Essa coleção vai carregar os erros
	 * E com esse metodo vou pecorrer essa coleção
	 * preechendo os erros na caixinha de texto do formulário
	 */
	private void setMensagemErros(Map<String, String> erros) {
		//Set é outra coleção, é o conjunto.
		Set<String> fields = erros.keySet();
		//vou pecorrer esse conjunto.
		//se nesse meu set de fields contem valor "name"
		//que no caso eu fiz lá em cima no getFormData
		//ESTOU TESTANDO SE NESSE CONJUNTO EXISTE A CHAVE NAME
		//SE EXISTIR VOU PEGAR O MEU LABELERRORNAME, e setar o texto dele
		//COM A MENSAGEM DE ERROR
		if(fields.contains("name")) 
		{
			//pegando a mensagem correspondente ao campo name
			//e setando no error que é labelErrorName
			labelErrorNome.setText(erros.get("name"));
		}
	}
	
}

package gui;

import java.net.URL;
import java.text.SimpleDateFormat;
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
import model.dao.FabricaDao;
import model.entidades.Vendedor;
import model.exceptions.ValidacaoException;
import model.servicos.VendedorServico;

public class VendedorFormController implements Initializable {
	
	
	
	private Vendedor entidadeVendedor;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	private VendedorServico servicoVendedor;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private TextField txtDataAniversario;
	
	@FXML
	private TextField txtSalarioBase;
	
	@FXML
	private TextField txtDepartamentoNome;
	
	//Botoes.
	
	@FXML
	private Button btSalvar;
	
	@FXML
	private Button btCancelar;
	
	//Erros
	@FXML
    private Label labelErrorNome;
    
    @FXML
    private Label labelErrorEmail;
    
    @FXML
    private Label labelErrorDataNascimento;
    
    @FXML
    private Label labelErrorSalarioBase;
    
    @FXML
    private Label labelErrorDepartamentoNome;
	
    //
    
    
    
    public void setVendedor(Vendedor entidadeVendedor) {
		this.entidadeVendedor = entidadeVendedor;
	}
	
	public void setVendedorServico(VendedorServico servicoVendedor) {
		this.servicoVendedor = servicoVendedor;
	}
	
	
	public void atualizarDadosFormulario() {
		//Programação defensiva
		//Vai que o programador esqueceu de injetar o departamento..
		if(entidadeVendedor== null) {
			throw new IllegalStateException("EntidadeVendedor está nula.");
		}
		
		
		
		//Jogar nas caixinha de texto, os dados que estão no tipo DepartamentoEntidade
		//Por que valueOf? por que a caixinha de texto trabalha com String
		//Ou seja vou ter que converter o entidadeDepartamento.getID que é inteiro
		//Para STRING
		txtId.setText(String.valueOf(entidadeVendedor.getId()));
		//
		txtNome.setText(entidadeVendedor.getNome());
		
		txtEmail.setText(entidadeVendedor.getEmail());
		
		if(entidadeVendedor.getDataNascimento() != null) {
			txtDataAniversario.setText(sdf.format(entidadeVendedor.getDataNascimento()));
		}
		
		txtSalarioBase.setText(String.valueOf(entidadeVendedor.getSalarioBase()));
		
		if(entidadeVendedor.getDepartamento() != null) {
			txtDepartamentoNome.setText(entidadeVendedor.getDepartamento().getNome());
		}
		
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
	
	@FXML
	public void onBtSalvarAcao(ActionEvent evento) {
		//Programação defensiva.
		if(entidadeVendedor == null) {
			//Ou seja vai que o programador esqueceu de enjetar..
			throw new IllegalStateException("Entidade está nulo");
		}
		if(servicoVendedor == null) {
			throw new IllegalStateException("Servico está nulo");
		}
		//Vou colocar tudo isso aqui abaixo dentro do try
		try {
			//Como se trata de um BD, pode gerar uma excessão.
			//Salva o departamento no BD
			//o meu departamento vai receber o getFormData
			//Responsável por pegar os dados que estão na caixinha do formulário
			//E instanciar um departamento para mim.
			entidadeVendedor = getFormData();
			//Salvei no banco de dados ou atualizei
			servicoVendedor.salvarOuAtualizar(entidadeVendedor);
			//criei esse metodo abaixo, que emite um evento na lista.
			notifyDataChangeListeners();
			//Fechando a janela.
			Utils.palcoAtual(evento).close();;
		}catch(ValidacaoException e) {
			setErrorMessages(e.getErros());
				
		}catch (DbException e) {
			Alerts.showAlert("Error salvando objeto", null, e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	private Vendedor getFormData() {
		limparErrorLabels();
		Vendedor obj = new Vendedor();
		ValidacaoException excessao = new ValidacaoException("Erro na Validação");
		
		
			obj.setId(Utils.tryParseToInt(txtId.getText()));
			//Como o Nome já é String vou pegar direto
			//Se o texto estiver nulo, ou se o texto eliminando os espaços em branco
			//Seja no inicio ou no final com o TRIM se isso aqui for igual
			//ao String vázio. Significa que a minha caixinha está vazia.
			if(txtNome.getText() == null || txtNome.getText().trim().equals("")) {
				excessao.addError("nome", "O campo não pode ser vázio");
			}
			
			obj.setNome(txtNome.getText().trim());
			
			
			//Email
			if(txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
				excessao.addError("email", "O campo não pode ficar vázio");
			}
			obj.setEmail(txtEmail.getText());
			
			//DataDeAniversário.
			if (txtDataAniversario.getText() == null || txtDataAniversario.getText().trim().equals("")) {
                excessao.addError("dataNascimento", "Field cannot be empty");
            } 
                
			obj.setDataNascimento(Utils.tryParseToDate(txtDataAniversario.getText()));
			
			//Salario base.
			
			if (txtSalarioBase.getText() == null || txtSalarioBase.getText().trim().equals("")) {
                excessao.addError("salarioBase", "Field cannot be empty");
            } else {
                obj.setSalarioBase(Double.parseDouble(txtSalarioBase.getText()));
            }
			
			//Departamento - Associacao.;
			
			if(txtDepartamentoNome.getText() == null || txtDepartamentoNome.getText().trim().equals("")) {
				excessao.addError("departamento", "O campo não pode está vazio!");
			}
			
			if(excessao.getErros().size() > 0) {
				throw excessao;
			}
			
			Vendedor novo = FabricaDao.criarVendedorDao().findById(Utils.tryParseToInt(txtDepartamentoNome.getText()));
			obj.setDepartamento(novo.getDepartamento());
		
		return obj;
	}
	
	private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();
        
        if (fields.contains("nome")) {
            labelErrorNome.setText(errors.get("nome"));
        }
        if (fields.contains("email")) {
            labelErrorEmail.setText(errors.get("email"));
        }
        if (fields.contains("dataNascimento")) {
            labelErrorDataNascimento.setText(errors.get("dataNascimento"));
        }
        if (fields.contains("salarioBase")) {
            labelErrorSalarioBase.setText(errors.get("salarioBase"));
        }
        if (fields.contains("departamento")) {
            labelErrorDepartamentoNome.setText(errors.get("departamento"));
        }
    }
	
	private void limparErrorLabels() {
		labelErrorNome.setText("");
		labelErrorEmail.setText("");
		labelErrorDataNascimento.setText("");
		labelErrorSalarioBase.setText("");
		labelErrorDepartamentoNome.setText("");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		
	}
	
	@FXML
    public void onBtCancelarAction(ActionEvent event) {
        Utils.palcoAtual(event).close();
    }
	
	public void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtNome, 30);
        Constraints.setTextFieldMaxLength(txtEmail, 50);
        Constraints.setTextFieldDouble(txtSalarioBase);
    }
	
	private void notifyDataChangeListeners() {
        dataChangeListeners.forEach(DataChangeListener::onDataChanged);
    }
	
	

}

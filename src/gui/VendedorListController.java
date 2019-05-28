package gui;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import aplicacao.Main;
import db.DbIntegrityException;
import gui.listener.DataChangeListener;
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
import javafx.scene.control.Alert;
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
import model.entidades.Departamento;
import model.entidades.Vendedor;
import model.servicos.VendedorServico;

public class VendedorListController implements Initializable, DataChangeListener {

	private VendedorServico vendedorServico;

	private ObservableList<Vendedor> obsList;

	@FXML
	private Button novoBotao;

	@FXML
	private TableView<Vendedor> tabelaViewVendedor;

	@FXML
	private TableColumn<Vendedor, Integer> tabelaColunaId;

	@FXML
	private TableColumn<Vendedor, String> tabelaColunaNome;

	@FXML
	private TableColumn<Vendedor, String> tabelaColunaEmail;

	@FXML
	private TableColumn<Vendedor, Date> tabelaColunaDataNascimento;

	@FXML
	private TableColumn<Vendedor, Double> tabelaColunaSalarioBase;

	@FXML
	private TableColumn<Vendedor, Departamento> tabelaColunaDepartamentoNome;

	@FXML
	private TableColumn<Vendedor, Vendedor> tabelaColunaEdit;

	@FXML
	private TableColumn<Vendedor, Vendedor> tabelaColunaRemove;

	@FXML
	public void onBtNovaAcao(ActionEvent event) {
		Stage stage = Utils.palcoAtual(event);
		Vendedor vendedor = new Vendedor();
		createDialogForm(vendedor, "/gui/VendedorForm.fxml", stage);
	}

	public void setVendedorServico(VendedorServico vendedorServico) {
		this.vendedorServico = vendedorServico;
	}

	public void atualizarTableView() {
		if (vendedorServico == null) {
			throw new IllegalStateException("Servico está nulo");
		} else {
			List<Vendedor> list = vendedorServico.findAll();
			obsList = FXCollections.observableArrayList(list);
			tabelaViewVendedor.setItems(obsList);
			initializeEditButtons();
			initializeRemoveButtons();
		}
	}

	@Override
	public void onDataChanged() {
		atualizarTableView();

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();

	}

	private void initializeNodes() {
		tabelaColunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tabelaColunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tabelaColunaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tabelaColunaDataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
		tabelaColunaSalarioBase.setCellValueFactory(new PropertyValueFactory<>("salarioBase"));
		tabelaColunaDepartamentoNome.setCellValueFactory(new PropertyValueFactory<>("departamento"));
		initializeDepartmentNameColumn();
		initializeDataNascimentoColumn();
		initializeEditButtons();
		initializeRemoveButtons();

		Stage mainStage = (Stage) Main.getCenaPrincipal().getWindow();
		tabelaViewVendedor.prefHeightProperty().bind(mainStage.heightProperty());

	}

	private void initializeDepartmentNameColumn() {

		tabelaColunaDepartamentoNome.setCellValueFactory(new PropertyValueFactory<>("departamento"));

		tabelaColunaDepartamentoNome.setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(Departamento departamento, boolean b) {
				super.updateItem(departamento, b);

				if (departamento == null) {
					setText(null);
				} else {
					setText(departamento.getNome());
				}
			}
		});
	}

	private void initializeDataNascimentoColumn() {
		tabelaColunaDataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));

		tabelaColunaDataNascimento.setCellFactory(param -> new TableCell<>() {
			private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			@Override
			public void updateItem(Date date, boolean empty) {
				if (date == null) {
					setText(null);
				} else {
					setText(sdf.format(date));
				}
			}
		});
	}

	private void initializeEditButtons() {
		tabelaColunaEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tabelaColunaEdit.setCellFactory(param -> new TableCell<>() {
			@FXML
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/VendedorForm.fxml", Utils.palcoAtual(event)));

			}
		});
	}

	private void initializeRemoveButtons() {
		tabelaColunaRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tabelaColunaRemove.setCellFactory(param -> new TableCell<>() {
			@FXML
			Button button = new Button("remove");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Vendedor obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmacao", "Tem certeza que deseja deletar?");

		if (result.get() == ButtonType.OK) {
			if (vendedorServico == null) {
				throw new IllegalStateException("Servico está nulo");
			}
			try {
				vendedorServico.remover(obj);
				atualizarTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Error removendo objeto", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

	private void createDialogForm(Vendedor vendedor, String absoluteName, Stage parentStage) {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			VendedorFormController controller = loader.getController();
			controller.setVendedor(vendedor);
			controller.setVendedorServico(new VendedorServico());
			controller.atualizarDadosFormulario();
			controller.subscribeDataChangeListener(this);

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Digite dados do vendedor: ");

			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
		}
	}

}

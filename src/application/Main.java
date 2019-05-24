package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	//Criando um atributo para ser referenciado a cena.
	private static Scene cenaPrincipal;
	
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			//Instanciando o objeto loader do tipo FXML
			//Isso vai ser importante para manipular a tela antes de carregar
			//Na instancia passa o caminho da view para ser carregada
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			//carregando a view
			/*
			Parent parent = loader.load();
			No lugar de parent vou colocar ScrollPane 
			para ele instanciar o objeto correto */
			ScrollPane scrollPane = loader.load();
			//Macete para ajustar a janela de acordo com a pag
			//ALTURA
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);
			
			
			//Cena principal
			//Scene mainScene = new Scene(scrollPane);
			//No lugar de declarar, vou só referenciar, para poder reutiliza-la
			cenaPrincipal = new Scene(scrollPane);
			//palco da cena, setando a cena como cena principal
			primaryStage.setScene(cenaPrincipal);
			//Titulo principal
			primaryStage.setTitle("Sample JavaFX application");
			//mostrando o palco
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Criando um metodo para pegar a referência statica que criei no começo.
	
	public static Scene getCenaPrincipal() {
		return cenaPrincipal;
	}
	

	public static void main(String[] args) {
		launch(args);
	}
}

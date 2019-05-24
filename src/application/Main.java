package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
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
			Scene mainScene = new Scene(scrollPane);
			//palco da cena, setando a cena como cena principal
			primaryStage.setScene(mainScene);
			//Titulo principal
			primaryStage.setTitle("Sample JavaFX application");
			//mostrando o palco
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}

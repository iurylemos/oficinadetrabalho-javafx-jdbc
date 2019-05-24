package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	/*
	 * Essa classe vai ter uma função para retornar do tipo Stage
	 */
	public static Stage palcoAtual(ActionEvent evento) {
		//Pegar o stage a partir do objeto de evento que é onde o Controller está.
		//getSource é do tipo Object ele é muito generico
		//Mas eu quero ele do tipo Node
		//Utilizo o getCena para pegar a cena
		//GetWindow é do tipo da janela
		//Mas como o GetWindow é de uma superclasse do Stage
		//Vou dar um downcasting para Stage
		return (Stage) ((Node) evento.getSource()).getScene().getWindow();
	}
}

package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	/*
	 * Essa classe vai ter uma fun��o para retornar do tipo Stage
	 */
	public static Stage palcoAtual(ActionEvent evento) {
		//Pegar o stage a partir do objeto de evento que � onde o Controller est�.
		//getSource � do tipo Object ele � muito generico
		//Mas eu quero ele do tipo Node
		//Utilizo o getCena para pegar a cena
		//GetWindow � do tipo da janela
		//Mas como o GetWindow � de uma superclasse do Stage
		//Vou dar um downcasting para Stage
		return (Stage) ((Node) evento.getSource()).getScene().getWindow();
	}
}

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
	
	/*
	 * Criar um metodo para transformar os metodos em inteiro.
	 * 
	 * Pode ser que o conteudo que voc� digitou l� na caixinha n�o seja valido
	 * Nesse caso vai ser lan�ado uma excess�o nesse parseInt
	 * Na verdade no lugar da excess�o vou retornar um valor nulo.
	 * 
	 * Ou eu vou converter o numero direitinho e retornar um numero
	 * ou retorna nulo
	 * ou seja n�o vai ter risco de ter excess�o
	 * S� para facilitar o nosso processo e n�o ter que ficar repetindo
	 * toda vez que precisar ler o dado de uma caixinha que � inteiro.
	 */
	
	
	public static Integer tryParseToInt(String str) {
		try {
			return Integer.parseInt(str);
		}catch(NumberFormatException e) {
			return null;
		}
	}
	
}

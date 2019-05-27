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
	
	/*
	 * Criar um metodo para transformar os metodos em inteiro.
	 * 
	 * Pode ser que o conteudo que você digitou lá na caixinha não seja valido
	 * Nesse caso vai ser lançado uma excessão nesse parseInt
	 * Na verdade no lugar da excessão vou retornar um valor nulo.
	 * 
	 * Ou eu vou converter o numero direitinho e retornar um numero
	 * ou retorna nulo
	 * ou seja não vai ter risco de ter excessão
	 * Só para facilitar o nosso processo e não ter que ficar repetindo
	 * toda vez que precisar ler o dado de uma caixinha que é inteiro.
	 */
	
	
	public static Integer tryParseToInt(String str) {
		try {
			return Integer.parseInt(str);
		}catch(NumberFormatException e) {
			return null;
		}
	}
	
}

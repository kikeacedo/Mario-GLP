package grammar;

import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class GPL {

	public static Derivation crearDerivacion() {
		Derivation d = null;
		Grammar g;

		try {
			g = new Grammar("Grammars/gramaticaMario.gr");
			d = new Derivation(g, 50);
			assertNotNull(d);
		} catch (GrammarException e) {
			e.printStackTrace();
		}//try-catch

		return d;

	}//crearComponentes
	
	public static ArrayList<String> getComponentes(Derivation der){
		ArrayList<String> componentes = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(der.getSpaceWord(), "%");
		
		while(st.hasMoreTokens())
			componentes.add(st.nextToken());

		for(int i = 0; i < componentes.size(); i++){
			System.out.println(componentes.get(i));
		}//for
		System.out.println("-------------------------------------");
		
		return componentes;
	}//getComponentes


	public static void toFile(ArrayList<String> componentes){
		try {
			PrintWriter out = new PrintWriter("Grammars/generada.txt");

			for(int i = 0; i < componentes.size(); i++){
				out.println(componentes.get(i));
			}//for

			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}//try-catch

	}//toFile

}//class
